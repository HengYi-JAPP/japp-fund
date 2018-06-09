/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Corporation} from '../../../shared/models/corporation';

export const INIT_SUCCESS = '[CorporationManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { corporations: Corporation[] }) {
  }
}

export const SET_FILTER_NAMEQ = '[CorporationManagePage] SET_FILTER_NAMEQ';

export class SetFilterNameQ implements Action {
  readonly type = SET_FILTER_NAMEQ;

  constructor(public payload?: string) {
  }
}

export const DELETE_SUCCESS = '[CorporationManagePage] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public payload: { id: string }) {
  }
}

export type Actions =
  | InitSuccess
  | SetFilterNameQ
  | DeleteSuccess;
