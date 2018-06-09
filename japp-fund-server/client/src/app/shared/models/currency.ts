import {LogableEntity} from './logable-entity';

export const CDHP = '_CDHP_';

export class Currency extends LogableEntity {
  code: string;
  name: string;
  sortBy: number;

  get isCdhp(): boolean {
    return this.code === '_CDHP_';
  }

  static assign(...sources: any[]): Currency {
    const result = Object.assign(new Currency(), ...sources);
    return result;
  }

  static toEntities(os: Currency[], entities?: { [id: string]: Currency }): { [id: string]: Currency } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.id] = Currency.assign(cur);
      return acc;
    }, {...(entities || {})});
  }
}
