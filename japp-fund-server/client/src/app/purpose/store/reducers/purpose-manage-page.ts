import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {Purpose} from '../../../shared/models/purpose';
import {purposeManagePageActions} from '../actions';

export interface State {
  entities: { [id: string]: Purpose };
}

const initialState: State = {
  entities: {},
};

export function reducer(state = initialState, action: purposeManagePageActions.Actions): State {
  switch (action.type) {

    case purposeManagePageActions.INIT_SUCCESS: {
      const {purposes} = action.payload;
      const entities = Purpose.toEntities(purposes);
      return {...state, entities};
    }

    case purposeManagePageActions.DELETE_SUCCESS: {
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
export const getPurposes = createSelector(getEntities, entities => Object.values(entities).sort(DefaultCompare));
