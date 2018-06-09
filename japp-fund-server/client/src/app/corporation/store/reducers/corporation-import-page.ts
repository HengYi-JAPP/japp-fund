import {createSelector} from '@ngrx/store';
import {CorporationSuggest} from '../../../shared/models/corporation-suggest';
import {corporationImportPageActions} from '../actions';

export interface State {
  entities: { [code: string]: CorporationSuggest };
}

const initialState: State = {
  entities: {},
};

export function reducer(state = initialState, action: corporationImportPageActions.Actions): State {
  switch (action.type) {
    case corporationImportPageActions.SEARCH_SUCCESS: {
      const {corporationSuggests} = action.payload;
      const entities = CorporationSuggest.toEntities(corporationSuggests);
      return {...state, entities};
    }
    case corporationImportPageActions.CREATE_SUCCESS: {
      const {corporation} = action.payload;
      const {code} = corporation;
      const entities = {...state.entities};
      delete entities[code];
      return {...state, entities};
    }

    default:
      return state;
  }
}

export const getEntities = (state: State) => state.entities;
export const getCorporationSuggests = createSelector(getEntities, entities => Object.values(entities));
