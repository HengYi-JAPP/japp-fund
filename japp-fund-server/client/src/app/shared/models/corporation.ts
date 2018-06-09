import {LogableEntity} from '../../shared/models/logable-entity';
import {Currency} from './currency';

export class Corporation extends LogableEntity {
  code: string;
  name: string;
  sortBy: number;
  currencies?: Currency[];

  static assign(...sources: any[]): Corporation {
    const result = Object.assign(new Corporation(), ...sources);
    return result;
  }

  static toEntities(os: Corporation[], entities?: { [id: string]: Corporation }): { [id: string]: Corporation } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.id] = Corporation.assign(cur);
      return acc;
    }, {...(entities || {})});
  }
}
