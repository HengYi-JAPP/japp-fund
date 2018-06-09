import {Entity} from '../../shared/models/entity';
import {OperatorGroup} from './operator-group';
import {OperatorPermission} from './operator-permission';
import {Permission} from './permission';

export class Operator extends Entity {
  name: string;
  nickName?: string;
  avatar?: string;
  hrId: string;
  oaId: string;
  admin: boolean;
  operatorGroups: OperatorGroup[] = [];
  permissions: Permission[] = [];

  static assign(...sources: any[]): Operator {
    const result = Object.assign(new Operator(), ...sources);
    return result;
  }

  static toEntities(os: Operator[], entities?: { [id: string]: Operator }): { [id: string]: Operator } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.id] = Operator.assign(cur);
      return acc;
    }, {...(entities || {})});
  }
}
