import {DefaultCrudRepository, repository, HasManyRepositoryFactory} from '@loopback/repository';
import {Company, CompanyRelations, Internship} from '../models';
import {DbDataSource} from '../datasources';
import {inject, Getter} from '@loopback/core';
import {InternshipRepository} from './internship.repository';

export class CompanyRepository extends DefaultCrudRepository<
  Company,
  typeof Company.prototype.id,
  CompanyRelations
> {

  public readonly internships: HasManyRepositoryFactory<Internship, typeof Company.prototype.id>;

  constructor(
    @inject('datasources.db') dataSource: DbDataSource, @repository.getter('InternshipRepository') protected internshipRepositoryGetter: Getter<InternshipRepository>,
  ) {
    super(Company, dataSource);
    this.internships = this.createHasManyRepositoryFactoryFor('internships', internshipRepositoryGetter,);
    this.registerInclusionResolver('internships', this.internships.inclusionResolver);
  }
}
