import {DEFAULT_ROLE_TYPES} from '../services/permission-role-type-name.pipe';

export class Permission {
  allCorporation: boolean;
  corporationIds: string[];
  allCurrency: boolean;
  currencyIds: string[];
  roleTypes: string[] = DEFAULT_ROLE_TYPES;

  static assign(...sources: any[]): Permission {
    const result = Object.assign(new Permission(), ...sources);
    return result;
  }
}
