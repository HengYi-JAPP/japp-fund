import {currencyImportPageActions} from '../actions';
import {createSelector} from '@ngrx/store';
import {CurrencySuggest} from '../../../shared/models/currency-suggest';

export interface State {
  codes: string[];
  entities: { [code: string]: CurrencySuggest };
}

const initialState: State = {
  codes: [],
  entities: {},
};

export function reducer(state = initialState, action: currencyImportPageActions.Actions): State {
  switch (action.type) {
    case currencyImportPageActions.CREATE_SUCCESS: {
      const entities = {...state.entities};
      delete entities[action.payload.code];
      const codes = Object.keys(entities);
      return {...state, codes, entities};
    }

    case currencyImportPageActions.SEARCH_SUCCESS: {
      const currencySuggests = action.payload || [];
      const entities = currencySuggests.reduce((acc, currencySuggest) => {
        acc[currencySuggest.code] = Object.assign({}, state.entities[currencySuggest.code], currencySuggest);
        return acc;
      }, {});
      const codes = Object.keys(entities);
      return {...state, codes, entities};
    }

    default:
      return state;
  }
}

export const codes = (state: State) => state.codes;
export const entities = (state: State) => state.entities;
export const corporationSuggests = createSelector(entities, codes, (entities, ids) => ids.map(id => entities[id]));
