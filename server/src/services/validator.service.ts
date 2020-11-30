import {HttpErrors} from '@loopback/rest';
import isemail from 'isemail';
import {Credentials} from '../repositories';

export function validateCredentials(credentials: Credentials) {
  // Validate Email
  if (!isemail.validate(credentials.email)) {
    throw new HttpErrors.UnprocessableEntity('invalid email');
  }

  // Validate Password Length
  if (!credentials.password || credentials.password.length < 6) {
    throw new HttpErrors.UnprocessableEntity(
      'password must be minimum 6 characters',
    );
  }
}
