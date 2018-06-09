/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Corporation} from '../../../shared/models/corporation';
import {CorporationSuggest} from '../../../shared/models/corporation-suggest';

export const SEARCH_SUCCESS = '[CorporationImportPage] SEARCH_SUCCESS';

export class SearchSuccess implements Action {
  readonly type = SEARCH_SUCCESS;

  constructor(public payload: { corporationSuggests: CorporationSuggest[] }) {
  }
}

export const CREATE_SUCCESS = '[CorporationImportPage] CREATE_SUCCESS';

export class CreateSuccess implements Action {
  readonly type = CREATE_SUCCESS;

  constructor(public payload: { corporation: Corporation }) {
  }
}

export type Actions =
  | SearchSuccess
  | CreateSuccess;
