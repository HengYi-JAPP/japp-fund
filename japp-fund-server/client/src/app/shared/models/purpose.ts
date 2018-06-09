import {LogableEntity} from './logable-entity';

export class Purpose extends LogableEntity {
  name: string;
  sortBy?: number;

  static assign(...sources: any[]): Purpose {
    const result = Object.assign(new Purpose(), ...sources);
    return result;
  }

  static toEntities(os: Purpose[], entities?: { [id: string]: Purpose }): { [id: string]: Purpose } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.id] = Purpose.assign(cur);
      return acc;
    }, {...(entities || {})});
  }
}
