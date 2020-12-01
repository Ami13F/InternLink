import {Entity, model, property, hasMany} from '@loopback/repository';
import {Internship} from './internship.model';

@model()
export class Company extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  id: string;

  @property({
    type: 'string',
    required: true,
  })
  name: string;

  @property({
    type: 'string',
    default: 'Enter description here...',
  })
  description?: string;

  @property({
    type: 'buffer',
    mysql: {
      dataType: "blob"
    }
  })
  avatar?: Buffer;

  @hasMany(() => Internship)
  internships: Internship[];

  constructor(data?: Partial<Company>) {
    super(data);
  }
}

export interface CompanyRelations {
  // describe navigational properties here
}

export type CompanyWithRelations = Company & CompanyRelations;
