import {createSelector} from '@ngrx/store';
import {CheckQ, DefaultCompare} from '../../../core/services/util.service';
import {Corporation} from '../../../shared/models/corporation';
import {corporationManagePageActions} from '../actions';

export interface State {
  entities: { [id: string]: Corporation };
  filterNameQ: string;
}

const initialState: State = {
  entities: {},
  filterNameQ: '',
};

export function reducer(state = initialState, action: corporationManagePageActions.Actions): State {
  switch (action.type) {
    case corporationManagePageActions.INIT_SUCCESS: {
      const {corporations} = action.payload;
      const entities = Corporation.toEntities(corporations);
      return {...state, entities};
    }

    case corporationManagePageActions.SET_FILTER_NAMEQ: {
      return {...state, filterNameQ: action.payload};
    }

    case corporationManagePageActions.DELETE_SUCCESS: {
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
export const getCorporations = createSelector(
  getEntities,
  getFilterNameQ,
  (entities, nameQ) => {
    const res = Object.values(entities)
      .filter(corporation => CheckQ(corporation.name, nameQ) || CheckQ(corporation.code, nameQ));
    return res.sort((o1, o2) => DefaultCompare(o1, o2));
  }
);
