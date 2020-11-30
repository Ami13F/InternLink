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
import {JobApplication, Student} from '../models';
import {StudentRepository} from '../repositories';

export class StudentApplicationController {
  constructor(
    @repository(StudentRepository)
    protected studentRepository: StudentRepository,
  ) {}

  @get('/students/{id}/applications', {
    responses: {
      '200': {
        description: 'Array of Student has many JobApplication',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(JobApplication)},
          },
        },
      },
    },
  })
  async find(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<JobApplication>,
  ): Promise<JobApplication[]> {
    return this.studentRepository.jobApplications(id).find(filter);
  }

  @post('/students/{id}/applications', {
    responses: {
      '200': {
        description: 'Student model instance',
        content: {
          'application/json': {schema: getModelSchemaRef(JobApplication)},
        },
      },
    },
  })
  async create(
    @param.path.string('id') id: typeof Student.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(JobApplication, {
            title: 'NewJobApplicationInStudent',
            exclude: ['id'],
            optional: ['studentId'],
          }),
        },
      },
    })
    jobApplication: Omit<JobApplication, 'id'>,
  ): Promise<JobApplication> {
    return this.studentRepository.jobApplications(id).create(jobApplication);
  }

  @patch('/students/{id}/applications', {
    responses: {
      '200': {
        description: 'Student.JobApplication PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(JobApplication, {partial: true}),
        },
      },
    })
    jobApplication: Partial<JobApplication>,
    @param.query.object('where', getWhereSchemaFor(JobApplication))
    where?: Where<JobApplication>,
  ): Promise<Count> {
    return this.studentRepository
      .jobApplications(id)
      .patch(jobApplication, where);
  }

  @del('/students/{id}/applications', {
    responses: {
      '200': {
        description: 'Student.JobApplication DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(JobApplication))
    where?: Where<JobApplication>,
  ): Promise<Count> {
    return this.studentRepository.jobApplications(id).delete(where);
  }
}
