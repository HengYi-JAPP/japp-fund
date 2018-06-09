/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Purpose} from '../../../shared/models/purpose';

export const INIT_SUCCESS = '[PurposeUpdatePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { purpose: Purpose }) {
  }
}

export type Actions =
  | InitSuccess;
