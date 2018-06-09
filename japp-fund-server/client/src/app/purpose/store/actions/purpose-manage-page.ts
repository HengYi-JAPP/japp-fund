/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Purpose} from '../../../shared/models/purpose';

export const INIT_SUCCESS = '[PurposeManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { purposes: Purpose[] }) {
  }
}

export const DELETE_SUCCESS = '[PurposeManagePage] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public payload: { id: string }) {
  }
}

export type Actions =
  | InitSuccess
  | DeleteSuccess;
