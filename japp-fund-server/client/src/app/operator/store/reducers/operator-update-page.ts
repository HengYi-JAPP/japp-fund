import {Operator} from '../../../shared/models/operator';
import {operatorUpdatePageActions} from '../actions';

export interface State {
  operator: Operator;
}

const initialState: State = {
  operator: null,
};

export function reducer(state = initialState, action: operatorUpdatePageActions.Actions): State {
  switch (action.type) {
    case operatorUpdatePageActions.INIT_SUCCESS: {
      const {operator} = action.payload;
      return {...state, operator};
    }

    default:
      return state;
  }
}

export const getOperator = (state: State) => state.operator;
