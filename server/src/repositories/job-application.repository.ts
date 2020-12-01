import {DefaultCrudRepository} from '@loopback/repository';
import {JobApplication, JobApplicationRelations} from '../models';
import {DbDataSource} from '../datasources';
import {inject} from '@loopback/core';

export class JobApplicationRepository extends DefaultCrudRepository<
  JobApplication,
  typeof JobApplication.prototype.id,
  JobApplicationRelations
> {
  constructor(
    @inject('datasources.db') dataSource: DbDataSource,
  ) {
    super(JobApplication, dataSource);
  }
}
