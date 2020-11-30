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
  Internship,
  AreaOfInterest,
} from '../models';
import {InternshipRepository} from '../repositories';

export class InternshipAreaOfInterestController {
  constructor(
    @repository(InternshipRepository) protected internshipRepository: InternshipRepository,
  ) { }

  @get('/internships/{id}/area-of-interests', {
    responses: {
      '200': {
        description: 'Array of Internship has many AreaOfInterest',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(AreaOfInterest)},
          },
        },
      },
    },
  })
  async find(
    @param.path.number('id') id: number,
    @param.query.object('filter') filter?: Filter<AreaOfInterest>,
  ): Promise<AreaOfInterest[]> {
    return this.internshipRepository.areasOfInterest(id).find(filter);
  }

  @post('/internships/{id}/area-of-interests', {
    responses: {
      '200': {
        description: 'Internship model instance',
        content: {'application/json': {schema: getModelSchemaRef(AreaOfInterest)}},
      },
    },
  })
  async create(
    @param.path.number('id') id: typeof Internship.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(AreaOfInterest, {
            title: 'NewAreaOfInterestInInternship',
            exclude: ['id'],
            optional: ['internshipId']
          }),
        },
      },
    }) areaOfInterest: Omit<AreaOfInterest, 'id'>,
  ): Promise<AreaOfInterest> {
    return this.internshipRepository.areasOfInterest(id).create(areaOfInterest);
  }

  @patch('/internships/{id}/area-of-interests', {
    responses: {
      '200': {
        description: 'Internship.AreaOfInterest PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.number('id') id: number,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(AreaOfInterest, {partial: true}),
        },
      },
    })
    areaOfInterest: Partial<AreaOfInterest>,
    @param.query.object('where', getWhereSchemaFor(AreaOfInterest)) where?: Where<AreaOfInterest>,
  ): Promise<Count> {
    return this.internshipRepository.areasOfInterest(id).patch(areaOfInterest, where);
  }

  @del('/internships/{id}/area-of-interests', {
    responses: {
      '200': {
        description: 'Internship.AreaOfInterest DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.number('id') id: number,
    @param.query.object('where', getWhereSchemaFor(AreaOfInterest)) where?: Where<AreaOfInterest>,
  ): Promise<Count> {
    return this.internshipRepository.areasOfInterest(id).delete(where);
  }
}
