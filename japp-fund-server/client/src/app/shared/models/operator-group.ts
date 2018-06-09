import {LogableEntity} from './logable-entity';
import {Permission} from './permission';

export class OperatorGroup extends LogableEntity {
  name: string;
  permissions: Permission[];

  static assign(...sources: any[]): OperatorGroup {
    const result = Object.assign(new OperatorGroup(), ...sources);
    return result;
  }

  static toEntities(os: OperatorGroup[], entities?: { [id: string]: OperatorGroup }): { [id: string]: OperatorGroup } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.id] = OperatorGroup.assign(cur);
      return acc;
    }, {...(entities || {})});
  }
}
