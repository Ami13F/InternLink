import {authenticate} from '@loopback/authentication';
import {authorize} from '@loopback/authorization';
import {
  Count,
  CountSchema,
  Filter,
  repository,
  Where,
} from '@loopback/repository';
import {
  del,
  get,
  getModelSchemaRef,
  getWhereSchemaFor,
  param,
  patch,
  post,
  requestBody,
} from '@loopback/rest';
import {basicAuthorization} from '../middlewares/auth.middle';
import {Company, User, UserType} from '../models';
import {UserRepository} from '../repositories';

@authenticate('jwt')
@authorize({
  allowedRoles: [UserType.Company],
  voters: [basicAuthorization],
})
export class UserCompanyController {
  constructor(
    @repository(UserRepository) protected userRepository: UserRepository,
  ) {}

  @get('/users/{id}/company', {
    responses: {
      '200': {
        description: 'User has one Company',
        content: {
          'application/json': {
            schema: getModelSchemaRef(Company),
          },
        },
      },
    },
  })
  async get(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<Company>,
  ): Promise<Company> {
    return this.userRepository.company(id).get(filter);
  }

  @post('/users/{id}/company', {
    responses: {
      '200': {
        description: 'User model instance',
        content: {'application/json': {schema: getModelSchemaRef(Company)}},
      },
    },
  })
  async create(
    @param.path.string('id') id: typeof User.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Company, {
            title: 'NewCompanyInUser',
            exclude: ['id'],
            optional: ['id'],
          }),
        },
      },
    })
    company: Omit<Company, 'id'>,
  ): Promise<Company> {
    return this.userRepository.company(id).create(company);
  }

  @patch('/users/{id}/company', {
    responses: {
      '200': {
        description: 'User.Company PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Company, {partial: true}),
        },
      },
    })
    company: Partial<Company>,
    @param.query.object('where', getWhereSchemaFor(Company))
    where?: Where<Company>,
  ): Promise<Count> {
    return this.userRepository.company(id).patch(company, where);
  }

  @del('/users/{id}/company', {
    responses: {
      '200': {
        description: 'User.Company DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(Company))
    where?: Where<Company>,
  ): Promise<Count> {
    return this.userRepository.company(id).delete(where);
  }
}
