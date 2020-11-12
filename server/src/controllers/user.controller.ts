import {model, property, repository} from '@loopback/repository';
import {PasswordHasher, validateCredentials} from '../services';
import {get, HttpErrors, param, post, requestBody} from '@loopback/rest';
import {User, UserType} from '../models';
import {Credentials, UserRepository} from '../repositories';
import {inject} from '@loopback/core';
import {
  authenticate,
  TokenService,
  UserService,
} from '@loopback/authentication';
import {authorize} from '@loopback/authorization';
import {SecurityBindings, securityId, UserProfile} from '@loopback/security';
import {
  CredentialsRequestBody,
  UserProfileSchema,
} from './specs/user-controller.specs';

import {
  PasswordHasherBindings,
  TokenServiceBindings,
  UserServiceBindings,
} from '../keys';
import _ from 'lodash';
import {basicAuthorization} from '../middlewares/auth.middle';

@model()
export class NewUserRequest extends User {
  @property({
    type: 'string',
    required: true,
  })
  password: string;
}

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

  private async createUser(
    newUserRequest: Credentials,
    userRole: string,
  ): Promise<User> {
    newUserRequest.role = userRole;

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

      return savedUser;
    } catch (error) {
      // MySQL error 1022 - duplicate key
      if (error.code === 1022 && error.errmsg.includes(`table: 'user'`)) {
        throw new HttpErrors.Conflict('Email value is already taken');
      } else {
        throw error;
      }
    }
  }

  @post('/users/sign-up/admin', {
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
  async createAdmin(
    @requestBody(CredentialsRequestBody)
    newUserRequest: Credentials,
  ): Promise<User> {
    return this.createUser(newUserRequest, UserType.Admin);
  }

  @post('/users/sign-up/company', {
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
  async createCompany(
    @requestBody(CredentialsRequestBody)
    newUserRequest: Credentials,
  ): Promise<User> {
    return this.createUser(newUserRequest, UserType.Company);
  }

  @post('/users/sign-up/student', {
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
  async createStudent(
    @requestBody(CredentialsRequestBody)
    newUserRequest: Credentials,
  ): Promise<User> {
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
            schema: UserProfileSchema,
          },
        },
      },
    },
  })
  @authenticate('jwt')
  async printCurrentUser(
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
              type: 'object',
              properties: {
                token: {
                  type: 'string',
                },
              },
            },
          },
        },
      },
    },
  })
  async login(
    @requestBody(CredentialsRequestBody) credentials: Credentials,
  ): Promise<{token: string}> {
    // ensure the user exists, and the password is correct
    const user = await this.userService.verifyCredentials(credentials);

    // convert a User object into a UserProfile object (reduced set of properties)
    const userProfile = this.userService.convertToUserProfile(user);

    // create a JSON Web Token based on the user profile
    const token = await this.jwtService.generateToken(userProfile);

    return {token};
  }
}
