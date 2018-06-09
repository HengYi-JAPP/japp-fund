/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {OperatorGroup} from '../../../shared/models/operator-group';

export const INIT_SUCCESS = '[OperatorGroupManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { operatorGroups: OperatorGroup[] }) {
  }
}

export const DELETE_SUCCESS = '[OperatorGroupManagePage] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public payload: { id: string }) {
  }
}

export type Actions =
  | InitSuccess
  | DeleteSuccess;
