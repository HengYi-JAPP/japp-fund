import {Corporation} from '../../../shared/models/corporation';
import {corporationUpdatePageActions} from '../actions';

export interface State {
  corporation: Corporation;
}

const initialState: State = {
  corporation: null,
};

export function reducer(state = initialState, action: corporationUpdatePageActions.Actions): State {
  switch (action.type) {
    case corporationUpdatePageActions.INIT_SUCCESS: {
      const {corporation} = action.payload;
      return {...state, corporation};
    }

    default:
      return state;
  }
}

export const getCorporation = (state: State) => state.corporation;
