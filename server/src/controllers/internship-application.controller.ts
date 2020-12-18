import {authenticate} from '@loopback/authentication';
import {
  Count,
  CountSchema,
  Filter,
  repository,
  Where
} from '@loopback/repository';
import {
  del,
  get,
  getModelSchemaRef,
  getWhereSchemaFor,
  param,
  patch,
  post,
  requestBody
} from '@loopback/rest';
import {
  Internship,
  JobApplication
} from '../models';
import {InternshipRepository} from '../repositories';

@authenticate('jwt')
export class InternshipApplicationController {
  constructor(
    @repository(InternshipRepository) protected internshipRepository: InternshipRepository,
  ) { }

  @get('/internships/{id}/applications', {
    responses: {
      '200': {
        description: 'Array of Internship has many JobApplication',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(JobApplication)},
          },
        },
      },
    },
  })
  async find(
    @param.path.number('id') id: number,
    @param.query.object('filter') filter?: Filter<JobApplication>,
  ): Promise<JobApplication[]> {
    return this.internshipRepository.jobApplications(id).find(filter);
  }

  @post('/internships/{id}/applications', {
    responses: {
      '200': {
        description: 'Internship model instance',
        content: {'application/json': {schema: getModelSchemaRef(JobApplication)}},
      },
    },
  })
  async create(
    @param.path.number('id') id: typeof Internship.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(JobApplication, {
            title: 'NewJobApplicationInInternship',
            exclude: ['id'],
            optional: ['internshipId']
          }),
        },
      },
    }) jobApplication: Omit<JobApplication, 'id'>,
  ): Promise<JobApplication> {
    return this.internshipRepository.jobApplications(id).create(jobApplication);
  }

  @patch('/internships/{id}/applications', {
    responses: {
      '200': {
        description: 'Internship.JobApplication PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.number('id') id: number,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(JobApplication, {partial: true}),
        },
      },
    })
    jobApplication: Partial<JobApplication>,
    @param.query.object('where', getWhereSchemaFor(JobApplication)) where?: Where<JobApplication>,
  ): Promise<Count> {
    return this.internshipRepository.jobApplications(id).patch(jobApplication, where);
  }

  @del('/internships/{id}/applications', {
    responses: {
      '200': {
        description: 'Internship.JobApplication DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.number('id') id: number,
    @param.query.object('where', getWhereSchemaFor(JobApplication)) where?: Where<JobApplication>,
  ): Promise<Count> {
    return this.internshipRepository.jobApplications(id).delete(where);
  }
}
