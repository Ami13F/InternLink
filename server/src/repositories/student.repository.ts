import {Getter, inject} from '@loopback/core';
import {
  DefaultCrudRepository,
  HasManyRepositoryFactory,
  repository,
} from '@loopback/repository';
import {MemoryDataSource} from '../datasources';
import {JobApplication, Student, StudentRelations} from '../models';
import {JobApplicationRepository} from './job-application.repository';

export class StudentRepository extends DefaultCrudRepository<
  Student,
  typeof Student.prototype.id,
  StudentRelations
> {
  public readonly jobApplications: HasManyRepositoryFactory<
    JobApplication,
    typeof Student.prototype.id
  >;

  constructor(
    @inject('datasources.memory') dataSource: MemoryDataSource,
    @repository.getter('JobApplicationRepository')
    protected jobApplicationRepositoryGetter: Getter<JobApplicationRepository>,
  ) {
    super(Student, dataSource);
    this.jobApplications = this.createHasManyRepositoryFactoryFor(
      'jobApplications',
      jobApplicationRepositoryGetter,
    );
    this.registerInclusionResolver(
      'jobApplications',
      this.jobApplications.inclusionResolver,
    );
  }
}
