import { inject } from '@loopback/core';
import { compare, genSalt, hash } from 'bcryptjs';
import { PasswordHasherBindings } from '../keys';

export interface PasswordHasher<T = string> {
  hashPassword(password: T): Promise<T>;
  comparePassword(providedPass: T, storedPass: T): Promise<boolean>;
}

export class BCryptHasher implements PasswordHasher<string> {
  constructor(
    @inject(PasswordHasherBindings.ROUNDS)
    private readonly rounds: number,
  ) { }
  
  async hashPassword(password: string): Promise<string> {
    const salt = await genSalt(this.rounds);
    return hash(password, salt);
  }

  async comparePassword(providedPass: string, storedPass: string): Promise<boolean> {
    return await compare(providedPass, storedPass);
  }
}