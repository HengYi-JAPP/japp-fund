import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {OperatorGroup} from '../../../shared/models/operator-group';
import {operatorGroupManagePageActions} from '../actions';

export interface State {
  entities: { [id: string]: OperatorGroup };
}

const initialState: State = {
  entities: {},
};

export function reducer(state = initialState, action: operatorGroupManagePageActions.Actions): State {
  switch (action.type) {
    case operatorGroupManagePageActions.INIT_SUCCESS: {
      const {operatorGroups} = action.payload;
      const entities = OperatorGroup.toEntities(operatorGroups);
      return {...state, entities};
    }

    case operatorGroupManagePageActions.DELETE_SUCCESS: {
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
export const getOperatorGroups = createSelector(getEntities, entities => Object.values(entities).sort(DefaultCompare));
