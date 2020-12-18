import {authenticate} from '@loopback/authentication';
import {Filter, repository} from '@loopback/repository';
import {get, getModelSchemaRef, param} from '@loopback/rest';
import {Internship} from '../models';
import {InternshipRepository} from '../repositories';

@authenticate('jwt')
export class InternshipController {
  constructor(
    @repository(InternshipRepository)
    protected internshipRepository: InternshipRepository,
  ) {}

  @get('/internships', {
    responses: {
      '200': {
        description: 'Get an Array of Internships',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Internship)},
          },
        },
      },
    },
  })
  async getInternships(
    @param.query.object('filter') filter?: Filter<Internship>,
  ): Promise<Internship[]> {
    return this.internshipRepository.find(filter);
  }

  @get('/internships/{id}', {
    responses: {
      '200': {
        description: 'Get an Array of Internships',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Internship)},
          },
        },
      },
    },
  })
  async getInternship(
    @param.path.number('id') id: typeof Internship.prototype.id,
  ): Promise<Internship> {
    return this.internshipRepository.findById(id);
  }
}
