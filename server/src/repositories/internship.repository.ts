import {Getter, inject} from '@loopback/core';
import {
  DefaultCrudRepository,
  HasManyRepositoryFactory,
  repository,
} from '@loopback/repository';
import {DbDataSource} from '../datasources';
import {Internship, InternshipRelations, JobApplication} from '../models';
import {JobApplicationRepository} from './job-application.repository';

export class InternshipRepository extends DefaultCrudRepository<
  Internship,
  typeof Internship.prototype.id,
  InternshipRelations
> {
  public readonly jobApplications: HasManyRepositoryFactory<
    JobApplication,
    typeof Internship.prototype.id
  >;

  constructor(
    @inject('datasources.db') dataSource: DbDataSource,
    @repository.getter('JobApplicationRepository')
    protected jobApplicationRepositoryGetter: Getter<JobApplicationRepository>,
  ) {
    super(Internship, dataSource);
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
