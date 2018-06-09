import {OperatorGroup} from '../../../shared/models/operator-group';
import {operatorGroupUpdatePageActions} from '../actions';

export interface State {
  operatorGroup: OperatorGroup;
}

const initialState: State = {
  operatorGroup: null,
};

export function reducer(state = initialState, action: operatorGroupUpdatePageActions.Actions): State {
  switch (action.type) {
    case operatorGroupUpdatePageActions.INIT_SUCCESS: {
      const {operatorGroup} = action.payload;
      return {...state, operatorGroup};
    }

    default:
      return state;
  }
}

export const getOperatorGroup = (state: State) => state.operatorGroup;
