import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MemoryDataSource} from '../datasources';
import {JobApplication, JobApplicationRelations} from '../models';

export class JobApplicationRepository extends DefaultCrudRepository<
  JobApplication,
  typeof JobApplication.prototype.id,
  JobApplicationRelations
> {
  constructor(@inject('datasources.memory') dataSource: MemoryDataSource) {
    super(JobApplication, dataSource);
  }
}
