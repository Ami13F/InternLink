import {DefaultCrudRepository} from '@loopback/repository';
import {AreaOfInterest, AreaOfInterestRelations} from '../models';
import {DbDataSource} from '../datasources';
import {inject} from '@loopback/core';

export class AreaOfInterestRepository extends DefaultCrudRepository<
  AreaOfInterest,
  typeof AreaOfInterest.prototype.id,
  AreaOfInterestRelations
> {
  constructor(
    @inject('datasources.db') dataSource: DbDataSource,
  ) {
    super(AreaOfInterest, dataSource);
  }
}
