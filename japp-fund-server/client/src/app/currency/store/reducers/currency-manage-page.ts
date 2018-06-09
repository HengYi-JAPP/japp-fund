import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {Currency} from '../../../shared/models/currency';
import {currencyManagePageActions} from '../actions';

export interface State {
  entities: { [id: string]: Currency };
}

const initialState: State = {
  entities: {},
};

export function reducer(state = initialState, action: currencyManagePageActions.Actions): State {
  switch (action.type) {
    case currencyManagePageActions.INIT_SUCCESS: {
      const {currencies} = action.payload;
      const entities = Currency.toEntities(currencies);
      return {...state, entities};
    }

    case currencyManagePageActions.DELETE_SUCCESS: {
      const {id} = action.payload;
      const entities = {...state.entities};
      delete entities[id];
      return {...state, entities};
    }

    default:
      return state;
  }
}

export const getEntities = (state: State) => state.entities;
export const getCurrencies = createSelector(getEntities, entities => Object.values(entities).sort(DefaultCompare));
