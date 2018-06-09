/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Currency} from '../../../shared/models/currency';
import {CurrencySuggest} from '../../../shared/models/currency-suggest';

export const CREATE = '[CurrencyImportPage] CREATE';
export const CREATE_SUCCESS = '[CurrencyImportPage] CREATE_SUCCESS';
export const SEARCH = '[CurrencyImportPage] SEARCH';
export const SEARCH_SUCCESS = '[CurrencyImportPage] SEARCH_SUCCESS';

export class Create implements Action {
  readonly type = CREATE;

  constructor(public payload: CurrencySuggest) {
  }
}

export class CreateSuccess implements Action {
  readonly type = CREATE_SUCCESS;

  constructor(public payload: Currency) {
  }
}


export class Search implements Action {
  readonly type = SEARCH;

  constructor(public payload: string) {
  }
}

export class SearchSuccess implements Action {
  readonly type = SEARCH_SUCCESS;

  constructor(public payload: CurrencySuggest[]) {
  }
}

export type Actions
  = Create
  | CreateSuccess
  | Search
  | SearchSuccess;
