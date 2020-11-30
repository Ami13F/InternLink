import {Entity, model, property} from '@loopback/repository';

@model()
export class AreaOfInterest extends Entity {
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
  name: string;

  @property({
    type: 'number',
    required: true,
  })
  internshipId: number;


  constructor(data?: Partial<AreaOfInterest>) {
    super(data);
  }
}

export interface AreaOfInterestRelations {
  // describe navigational properties here
}

export type AreaOfInterestWithRelations = AreaOfInterest & AreaOfInterestRelations;
