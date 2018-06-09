import {OperatorGroup} from './operator-group';
import {Permission} from './permission';

export class OperatorPermission {
  operatorGroups: OperatorGroup[] = [];
  permissions: Permission[] = [];
}
