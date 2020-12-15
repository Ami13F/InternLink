import {Getter, inject} from '@loopback/core';
import {
  DefaultCrudRepository,
  HasManyRepositoryFactory,
  repository,
} from '@loopback/repository';
import {DbDataSource} from '../datasources';
import {Company, CompanyRelations, Internship} from '../models';
import {InternshipRepository} from './internship.repository';

export class CompanyRepository extends DefaultCrudRepository<
  Company,
  typeof Company.prototype.id,
  CompanyRelations
> {
  public readonly internships: HasManyRepositoryFactory<
    Internship,
    typeof Company.prototype.id
  >;

  constructor(
    @inject('datasources.db') dataSource: DbDataSource,
    @repository.getter('InternshipRepository')
    protected internshipRepositoryGetter: Getter<InternshipRepository>,
  ) {
    super(Company, dataSource);
    this.internships = this.createHasManyRepositoryFactoryFor(
      'internships',
      internshipRepositoryGetter,
    );
    this.registerInclusionResolver(
      'internships',
      this.internships.inclusionResolver,
    );
  }
}
