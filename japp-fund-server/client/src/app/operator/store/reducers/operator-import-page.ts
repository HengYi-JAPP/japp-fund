import {createSelector} from '@ngrx/store';
import {OperatorSuggest} from '../../../shared/models/operator-suggest';
import {operatorImportPageActions} from '../actions';

export interface State {
  entities: { [key: string]: OperatorSuggest };
}

const initialState: State = {
  entities: {},
};

export function reducer(state = initialState, action: operatorImportPageActions.Actions): State {
  switch (action.type) {
    case operatorImportPageActions.SEARCH_SUCCESS: {
      const {operatorSuggests} = action.payload;
      const entities = OperatorSuggest.toEntities(operatorSuggests);
      return {...state, entities};
    }

    default:
      return state;
  }
}

export const getEntities = (state: State) => state.entities;
export const getOperatorSuggests = createSelector(getEntities, entities => Object.values(entities));
