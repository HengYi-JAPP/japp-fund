/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Operator} from '../../../shared/models/operator';

export const INIT_SUCCESS = '[OperatorManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { operators: Operator[] }) {
  }
}

export const SET_FILTER_NAMEQ = '[OperatorManagePage] SET_FILTER_NAMEQ';

export class SetFilterNameQ implements Action {
  readonly type = SET_FILTER_NAMEQ;

  constructor(public payload?: string) {
  }
}

export const DELETE_SUCCESS = '[OperatorManagePage] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public payload: { id: string }) {
  }
}

export type Actions =
  | InitSuccess
  | SetFilterNameQ
  | DeleteSuccess;
