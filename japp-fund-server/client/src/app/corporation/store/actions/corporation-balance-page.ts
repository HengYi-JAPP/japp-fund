/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Corporation} from '../../../shared/models/corporation';
import {CorporationBalance} from '../../../shared/models/corporation-balance';
import {Currency} from '../../../shared/models/currency';

export const INIT = '[CorporationBalancePage] INIT';
export const SET_CORPORATION = '[CorporationBalancePage] SET_CORPORATION';
export const SET_CURRENCIES = '[CorporationBalancePage] SET_CURRENCIES';
export const LIST = '[CorporationBalancePage] LIST';

export class Init implements Action {
  readonly type = INIT;

  constructor(public payload: string) {
  }
}

export class SetCorporation implements Action {
  readonly type = SET_CORPORATION;

  constructor(public payload: Corporation) {
  }
}

export class SetCurrencies implements Action {
  readonly type = SET_CURRENCIES;

  constructor(public payload: Currency[]) {
  }
}

export class List implements Action {
  readonly type = LIST;

  constructor(public payload: { corporationId: string; currencyId: string; year: number; month: number; date?: Date }) {
  }
}

export const LIST_SUCCESS = '[CorporationBalancePage] LIST_SUCCESS';

export class ListSuccess implements Action {
  readonly type = LIST_SUCCESS;

  constructor(public payload: { balances: CorporationBalance[]; corporationId: string; currencyId: string; year: number; month: number; date?: Date }) {
  }
}

export type Actions
  = Init
  | SetCorporation
  | SetCurrencies
  | List
  | ListSuccess;
