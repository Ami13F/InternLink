import {Model, model, property} from '@loopback/repository';
import {UserType} from './user-type.model';

@model()
export class UserAuthorization extends Model {
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

  @property({
    type: 'string',
    required: true,
    jsonSchema: {
      enum: Object.values(UserType),
    },
  })
  role: string;

  constructor(data?: Partial<UserAuthorization>) {
    super(data);
  }
}

export interface UserAuthorizationRelations {
  // describe navigational properties here
}

export type UserAuthorizationWithRelations = UserAuthorization &
  UserAuthorizationRelations;
