import {DefaultCrudRepository, repository, HasManyRepositoryFactory} from '@loopback/repository';
import {Student, StudentRelations, JobApplication} from '../models';
import {DbDataSource} from '../datasources';
import {inject, Getter} from '@loopback/core';
import {JobApplicationRepository} from './job-application.repository';

export class StudentRepository extends DefaultCrudRepository<
  Student,
  typeof Student.prototype.id,
  StudentRelations
> {

  public readonly jobApplications: HasManyRepositoryFactory<JobApplication, typeof Student.prototype.id>;

  constructor(
    @inject('datasources.db') dataSource: DbDataSource, @repository.getter('JobApplicationRepository') protected jobApplicationRepositoryGetter: Getter<JobApplicationRepository>,
  ) {
    super(Student, dataSource);
    this.jobApplications = this.createHasManyRepositoryFactoryFor('jobApplications', jobApplicationRepositoryGetter,);
    this.registerInclusionResolver('jobApplications', this.jobApplications.inclusionResolver);
  }
}
