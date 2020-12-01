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
import {
  Company,
  Internship,
} from '../models';
import {CompanyRepository} from '../repositories';

export class CompanyInternshipController {
  constructor(
    @repository(CompanyRepository) protected companyRepository: CompanyRepository,
  ) { }

  @get('/companies/{id}/internships', {
    responses: {
      '200': {
        description: 'Array of Company has many Internship',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Internship)},
          },
        },
      },
    },
  })
  async find(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<Internship>,
  ): Promise<Internship[]> {
    return this.companyRepository.internships(id).find(filter);
  }

  @post('/companies/{id}/internships', {
    responses: {
      '200': {
        description: 'Company model instance',
        content: {'application/json': {schema: getModelSchemaRef(Internship)}},
      },
    },
  })
  async create(
    @param.path.string('id') id: typeof Company.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Internship, {
            title: 'NewInternshipInCompany',
            exclude: ['id'],
            optional: ['companyId']
          }),
        },
      },
    }) internship: Omit<Internship, 'id'>,
  ): Promise<Internship> {
    return this.companyRepository.internships(id).create(internship);
  }

  @patch('/companies/{id}/internships', {
    responses: {
      '200': {
        description: 'Company.Internship PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Internship, {partial: true}),
        },
      },
    })
    internship: Partial<Internship>,
    @param.query.object('where', getWhereSchemaFor(Internship)) where?: Where<Internship>,
  ): Promise<Count> {
    return this.companyRepository.internships(id).patch(internship, where);
  }

  @del('/companies/{id}/internships', {
    responses: {
      '200': {
        description: 'Company.Internship DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(Internship)) where?: Where<Internship>,
  ): Promise<Count> {
    return this.companyRepository.internships(id).delete(where);
  }
}
