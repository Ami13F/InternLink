import {
  authenticate,
  TokenService,
  UserService,
} from '@loopback/authentication';
import {authorize} from '@loopback/authorization';
import {inject} from '@loopback/core';
import {repository} from '@loopback/repository';
import {get, HttpErrors, param, post, requestBody} from '@loopback/rest';
import {SecurityBindings, securityId, UserProfile} from '@loopback/security';
import _ from 'lodash';
import {
  PasswordHasherBindings,
  TokenServiceBindings,
  UserServiceBindings,
} from '../keys';
import {basicAuthorization} from '../middlewares/auth.middle';
import {User, UserResponse, UserType} from '../models';
import {Credentials, UserRepository} from '../repositories';
import {PasswordHasher, validateCredentials} from '../services';
import {CredentialsRequestBody} from './specs/user-controller.specs';

export class UserController {
  constructor(
    @repository(UserRepository) public userRepository: UserRepository,
    @inject(PasswordHasherBindings.PASSWORD_HASHER)
    public passwordHasher: PasswordHasher,
    @inject(TokenServiceBindings.TOKEN_SERVICE)
    public jwtService: TokenService,
    @inject(UserServiceBindings.USER_SERVICE)
    public userService: UserService<User, Credentials>,
  ) {}

  @post('/users/sign-up/admin', {
    responses: {
      '200': {
        description: 'User response',
        content: {
          'application/json': {
            schema: {
              'x-ts-type': UserResponse,
            },
          },
        },
      },
    },
  })
  async createAdmin(
    @requestBody(CredentialsRequestBody)
    newUserRequest: Credentials,
  ): Promise<UserResponse> {
    return this.createUser(newUserRequest, UserType.Admin);
  }

  @post('/users/sign-up/company', {
    responses: {
      '200': {
        description: 'User response',
        content: {
          'application/json': {
            schema: {
              'x-ts-type': UserResponse,
            },
          },
        },
      },
    },
  })
  async createCompany(
    @requestBody(CredentialsRequestBody)
    newUserRequest: Credentials,
  ): Promise<UserResponse> {
    return this.createUser(newUserRequest, UserType.Company);
  }

  @post('/users/sign-up/student', {
    responses: {
      '200': {
        description: 'User response',
        content: {
          'application/json': {
            schema: {
              'x-ts-type': UserResponse,
            },
          },
        },
      },
    },
  })
  async createStudent(
    @requestBody(CredentialsRequestBody)
    newUserRequest: Credentials,
  ): Promise<UserResponse> {
    return this.createUser(newUserRequest, UserType.Student);
  }

  @get('/users/{userId}', {
    responses: {
      '200': {
        description: 'User',
        content: {
          'application/json': {
            schema: {
              'x-ts-type': User,
            },
          },
        },
      },
    },
  })
  @authenticate('jwt')
  @authorize({
    allowedRoles: [UserType.Admin],
    voters: [basicAuthorization],
  })
  async findById(@param.path.string('userId') userId: string): Promise<User> {
    return this.userRepository.findById(userId);
  }

  @get('/users/me', {
    responses: {
      '200': {
        description: 'The current user profile',
        content: {
          'application/json': {
            schema: {
              'x-ts-type': User,
            },
          },
        },
      },
    },
  })
  @authenticate('jwt')
  async getCurrentUser(
    @inject(SecurityBindings.USER)
    currentUserProfile: UserProfile,
  ): Promise<User> {
    const userId = currentUserProfile[securityId];
    return this.userRepository.findById(userId);
  }

  @post('/users/login', {
    responses: {
      '200': {
        description: 'Token',
        content: {
          'application/json': {
            schema: {
              'x-ts-type': UserResponse,
            },
          },
        },
      },
    },
  })
  async login(
    @requestBody(CredentialsRequestBody) credentials: Credentials,
  ): Promise<UserResponse> {
    return this.loginUser(credentials);
  }

  private async loginUser(credentials: Credentials): Promise<UserResponse> {
    // ensure the user exists, and the password is correct
    const user = await this.userService.verifyCredentials(credentials);

    // convert a User object into a UserProfile object (reduced set of properties)
    const userProfile = this.userService.convertToUserProfile(user);

    // create a JSON Web Token based on the user profile
    const token = await this.jwtService.generateToken(userProfile);

    return new UserResponse({id: user.id, token: token});
  }

  private async createUser(
    newUserRequest: Credentials,
    userRole: UserType,
  ): Promise<UserResponse> {
    newUserRequest.role = userRole.toString();

    // ensure a valid email value and password value
    validateCredentials(_.pick(newUserRequest, ['email', 'password']));

    // encrypt the password
    const password = await this.passwordHasher.hashPassword(
      newUserRequest.password,
    );

    try {
      // create the new user
      const savedUser = await this.userRepository.create(
        _.omit(newUserRequest, 'password'),
      );

      // set the password
      await this.userRepository
        .userCredentials(savedUser.id)
        .create({password});

      // login the user
      return await this.loginUser(newUserRequest);
    } catch (error) {
      // MySQL error 1062 - duplicate key
      if (error.errno === 1062 && error.code === `ER_DUP_ENTRY`) {
        throw new HttpErrors.Conflict('Email value is already taken');
      } else {
        throw error;
      }
    }
  }
}
