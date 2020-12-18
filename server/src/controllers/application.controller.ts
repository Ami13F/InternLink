import {authenticate} from '@loopback/authentication';
import {Filter, repository} from '@loopback/repository';
import {get, getModelSchemaRef, param} from '@loopback/rest';
import {JobApplication} from '../models';
import {JobApplicationRepository} from '../repositories';

@authenticate('jwt')
export class JobApplicationController {
  constructor(
    @repository(JobApplicationRepository)
    protected applicationRepository: JobApplicationRepository,
  ) {}

  @get('/applications', {
    responses: {
      '200': {
        description: 'Get an Array of job Applications',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(JobApplication)},
          },
        },
      },
    },
  })
  async getApplications(
    @param.query.object('filter') filter?: Filter<JobApplication>,
  ): Promise<JobApplication[]> {
    return this.applicationRepository.find(filter);
  }
}
