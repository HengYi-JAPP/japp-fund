/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Currency} from '../../../shared/models/currency';

export const INIT_SUCCESS = '[CurrencyUpdatePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { currency: Currency }) {
  }
}

export type Actions =
  | InitSuccess;
