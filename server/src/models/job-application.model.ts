import {Entity, model, property} from '@loopback/repository';
import {ApplicationStatus} from './application-status.model';

@model()
export class JobApplication extends Entity {
  @property({
    type: 'number',
    id: true,
    generated: true,
  })
  id?: number;

  @property({
    type: 'string',
    default: ApplicationStatus.Pending,
    jsonSchema: {
      enum: Object.values(ApplicationStatus),
    },
  })
  status: string;

  @property({
    type: 'string',
    required: true,
  })
  studentId: string;

  @property({
    type: 'number',
    required: true,
  })
  internshipId: number;

  constructor(data?: Partial<JobApplication>) {
    super(data);
  }
}

export interface JobApplicationRelations {
  // describe navigational properties here
}

export type JobApplicationWithRelations = JobApplication &
  JobApplicationRelations;
