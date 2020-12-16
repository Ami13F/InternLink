import {authenticate} from '@loopback/authentication';
import {repository} from '@loopback/repository';
import {get, getModelSchemaRef, param} from '@loopback/rest';
import {Company} from '../models';
import {CompanyRepository} from '../repositories';

@authenticate('jwt')
export class CompanyController {
  constructor(
    @repository(CompanyRepository)
    protected companyRepository: CompanyRepository,
  ) {}

  @get('/companies/{id}', {
    responses: {
      '200': {
        description: 'Get a Company',
        content: {
          'application/json': {
            schema: getModelSchemaRef(Company),
          },
        },
      },
    },
  })
  async getCompany(
    @param.path.string('id') id: typeof Company.prototype.id,
  ): Promise<Company> {
    return this.companyRepository.findById(id);
  }
}
