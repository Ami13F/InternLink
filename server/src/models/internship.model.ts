import {Entity, model, property, hasMany} from '@loopback/repository';
import {AreaOfInterest} from './area-of-interest.model';
import {JobApplication} from './job-application.model';

@model()
export class Internship extends Entity {
  @property({
    type: 'number',
    id: true,
    generated: true,
  })
  id?: number;

  @property({
    type: 'string',
    required: true,
  })
  title: string;

  @property({
    type: 'string',
    required: true,
  })
  location: string;

  @property({
    type: 'date',
  })
  deadline?: string;

  @property({
    type: 'date',
    required: true,
  })
  startDate: string;

  @property({
    type: 'date',
    required: true,
  })
  endDate: string;

  @property({
    type: 'string',
    default: "Enter description here...",
  })
  description?: string;

  @property({
    type: 'boolean',
    default: false,
  })
  isPaid?: boolean;

  @property({
    type: 'string',
    required: true,
  })
  companyId: string;

  @hasMany(() => AreaOfInterest)
  areasOfInterest: AreaOfInterest[];

  @hasMany(() => JobApplication)
  jobApplications: JobApplication[];

  constructor(data?: Partial<Internship>) {
    super(data);
  }
}

export interface InternshipRelations {
  // describe navigational properties here
}

export type InternshipWithRelations = Internship & InternshipRelations;
