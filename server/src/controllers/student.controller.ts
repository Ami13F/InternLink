import {authenticate} from '@loopback/authentication';
import {Filter, repository} from '@loopback/repository';
import {get, getModelSchemaRef, param} from '@loopback/rest';
import {JobApplication, Student} from '../models';
import {StudentRepository} from '../repositories';

@authenticate('jwt')
export class StudentController {
  constructor(
    @repository(StudentRepository)
    protected applicationRepository: StudentRepository,
  ) {}

  @get('/students/{id}', {
    responses: {
      '200': {
        description: 'Get a Student',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(JobApplication)},
          },
        },
      },
    },
  })
  async getStudent(
    @param.path.string('id') id: typeof Student.prototype.id,
  ): Promise<Student> {
    return this.applicationRepository.findById(id);
  }
}
