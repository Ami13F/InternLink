import {DefaultCrudRepository, repository, HasManyRepositoryFactory} from '@loopback/repository';
import {Internship, InternshipRelations, AreaOfInterest, JobApplication} from '../models';
import {DbDataSource} from '../datasources';
import {inject, Getter} from '@loopback/core';
import {AreaOfInterestRepository} from './area-of-interest.repository';
import {JobApplicationRepository} from './job-application.repository';

export class InternshipRepository extends DefaultCrudRepository<
  Internship,
  typeof Internship.prototype.id,
  InternshipRelations
> {

  public readonly areasOfInterest: HasManyRepositoryFactory<AreaOfInterest, typeof Internship.prototype.id>;

  public readonly jobApplications: HasManyRepositoryFactory<JobApplication, typeof Internship.prototype.id>;

  constructor(
    @inject('datasources.db') dataSource: DbDataSource, @repository.getter('AreaOfInterestRepository') protected areaOfInterestRepositoryGetter: Getter<AreaOfInterestRepository>, @repository.getter('JobApplicationRepository') protected jobApplicationRepositoryGetter: Getter<JobApplicationRepository>,
  ) {
    super(Internship, dataSource);
    this.jobApplications = this.createHasManyRepositoryFactoryFor('jobApplications', jobApplicationRepositoryGetter,);
    this.registerInclusionResolver('jobApplications', this.jobApplications.inclusionResolver);
    this.areasOfInterest = this.createHasManyRepositoryFactoryFor('areasOfInterest', areaOfInterestRepositoryGetter,);
    this.registerInclusionResolver('areasOfInterest', this.areasOfInterest.inclusionResolver);
  }
}
