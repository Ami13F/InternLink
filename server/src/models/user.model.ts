import {Entity, hasOne, model, property} from '@loopback/repository';
import {Company} from './company.model';
import {Student} from './student.model';
import {UserCredentials} from './user-credentials.model';
import {UserType} from './user-type.model';

@model({
  settings: {
    indexes: {
      uniqueEmail: {
        keys: {
          email: 1,
        },
        options: {
          unique: true,
        },
      },
    },
  },
})
export class User extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
    defaultFn: 'uuidv4',
  })
  id: string;

  @property({
    type: 'string',
    required: true,
  })
  email: string;

  @property({
    type: 'string',
    required: true,
    jsonSchema: {
      enum: Object.values(UserType),
    },
  })
  role: string;

  @hasOne(() => UserCredentials)
  userCredentials: UserCredentials;

  @hasOne(() => Student, {keyTo: 'id'})
  student: Student;

  @hasOne(() => Company, {keyTo: 'id'})
  company: Company;

  constructor(data?: Partial<User>) {
    super(data);
  }
}

export interface UserRelations {
  // describe navigational properties here
}

export type UserWithRelations = User & UserRelations;
