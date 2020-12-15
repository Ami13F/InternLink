import {Getter, inject} from '@loopback/core';
import {
  DefaultCrudRepository,
  HasOneRepositoryFactory,
  repository,
} from '@loopback/repository';
import {MemoryDataSource} from '../datasources';
import {
  Company,
  Student,
  User,
  UserCredentials,
  UserRelations,
} from '../models';
import {CompanyRepository} from './company.repository';
import {StudentRepository} from './student.repository';
import {UserCredentialsRepository} from './user-credentials.repository';

export type Credentials = {
  email: string;
  password: string;
  role?: string;
};

export class UserRepository extends DefaultCrudRepository<
  User,
  typeof User.prototype.id,
  UserRelations
> {
  public readonly userCredentials: HasOneRepositoryFactory<
    UserCredentials,
    typeof User.prototype.id
  >;

  public readonly student: HasOneRepositoryFactory<
    Student,
    typeof User.prototype.id
  >;

  public readonly company: HasOneRepositoryFactory<
    Company,
    typeof User.prototype.id
  >;

  constructor(
    @inject('datasources.memory') dataSource: MemoryDataSource,
    @repository.getter('UserCredentialsRepository')
    protected userCredentialsRepositoryGetter: Getter<
      UserCredentialsRepository
    >,
    @repository.getter('StudentRepository')
    protected studentRepositoryGetter: Getter<StudentRepository>,
    @repository.getter('CompanyRepository')
    protected companyRepositoryGetter: Getter<CompanyRepository>,
  ) {
    super(User, dataSource);
    this.company = this.createHasOneRepositoryFactoryFor(
      'company',
      companyRepositoryGetter,
    );
    this.registerInclusionResolver('company', this.company.inclusionResolver);
    this.student = this.createHasOneRepositoryFactoryFor(
      'student',
      studentRepositoryGetter,
    );
    this.registerInclusionResolver('student', this.student.inclusionResolver);
    this.userCredentials = this.createHasOneRepositoryFactoryFor(
      'userCredentials',
      userCredentialsRepositoryGetter,
    );
    this.registerInclusionResolver(
      'userCredentials',
      this.userCredentials.inclusionResolver,
    );
  }

  async findCredentials(
    userId: typeof User.prototype.id,
  ): Promise<UserCredentials | undefined> {
    try {
      return await this.userCredentials(userId).get();
    } catch (err) {
      if (err.code === 'ENTITY_NOT_FOUND') {
        return undefined;
      }
      throw err;
    }
  }
}
