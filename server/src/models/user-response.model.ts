import {Model, model, property} from '@loopback/repository';

@model()
export class UserResponse extends Model {
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
  token: string;

  constructor(data?: Partial<UserResponse>) {
    super(data);
  }
}

export interface UserResponseRelations {
  // describe navigational properties here
}

export type UserResponseWithRelations = UserResponse & UserResponseRelations;
