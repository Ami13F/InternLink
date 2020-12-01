import {Entity, model, property, hasMany} from '@loopback/repository';
import {JobApplication} from './job-application.model';

@model()
export class Student extends Entity {
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
  firstName: string;

  @property({
    type: 'string',
    required: true,
  })
  lastName: string;

  @property({
    type: 'string',
    default: 'Enter description here...',
  })
  description?: string;

  @property({
    type: 'string',
    required: true,
  })
  country: string;

  @property({
    type: 'buffer',
    mysql: {
      dataType: "blob"
    }
  })
  avatar?: Buffer;

  @hasMany(() => JobApplication)
  jobApplications: JobApplication[];

  constructor(data?: Partial<Student>) {
    super(data);
  }
}

export interface StudentRelations {
  // describe navigational properties here
}

export type StudentWithRelations = Student & StudentRelations;
