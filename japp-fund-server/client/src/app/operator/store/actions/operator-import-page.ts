/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {OperatorSuggest} from '../../../shared/models/operator-suggest';

export const SEARCH_SUCCESS = '[OperatorImportPage] SEARCH_SUCCESS';

export class SearchSuccess implements Action {
  readonly type = SEARCH_SUCCESS;

  constructor(public payload: { operatorSuggests: OperatorSuggest[] }) {
  }
}

export type Actions =
  | SearchSuccess;
