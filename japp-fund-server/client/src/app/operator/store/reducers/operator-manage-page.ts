import {createSelector} from '@ngrx/store';
import {CheckQ, DefaultCompare} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {operatorManagePageActions} from '../actions';

export interface State {
  entities: { [id: string]: Operator };
  filterNameQ: string;
}

const initialState: State = {
  entities: {},
  filterNameQ: '',
};

export function reducer(state = initialState, action: operatorManagePageActions.Actions): State {
  switch (action.type) {
    case operatorManagePageActions.INIT_SUCCESS: {
      const {operators} = action.payload;
      const entities = Operator.toEntities(operators);
      return {...state, filterNameQ: '', entities};
    }

    case operatorManagePageActions.SET_FILTER_NAMEQ: {
      return {...state, filterNameQ: action.payload};
    }

    case operatorManagePageActions.DELETE_SUCCESS: {
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
export const getFilterNameQ = (state: State) => state.filterNameQ;
export const getOperators = createSelector(getEntities, getFilterNameQ, (entities, nameQ) => {
  const res = Object.values(entities)
    .filter(operator => CheckQ(operator.name, nameQ));
  return res.sort((o1, o2) => DefaultCompare(o1, o2));
});
