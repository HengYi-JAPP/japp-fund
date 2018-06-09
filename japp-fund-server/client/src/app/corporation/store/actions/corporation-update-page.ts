/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Corporation} from '../../../shared/models/corporation';

export const INIT_SUCCESS = '[CorporationUpdatePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { corporation: Corporation }) {
  }
}

export type Actions =
  | InitSuccess;
