/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Currency} from '../../../shared/models/currency';

export const INIT_SUCCESS = '[CurrencyManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { currencies: Currency[] }) {
  }
}

export const DELETE_SUCCESS = '[CurrencyManagePage] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public payload: { id: string }) {
  }
}

export type Actions =
  | InitSuccess
  | DeleteSuccess;
