import {Purpose} from '../../../shared/models/purpose';
import {purposeUpdatePageActions} from '../actions';

export interface State {
  purpose: Purpose;
}

const initialState: State = {
  purpose: null,
};

export function reducer(state = initialState, action: purposeUpdatePageActions.Actions): State {
  switch (action.type) {
    case purposeUpdatePageActions.INIT_SUCCESS: {
      const {purpose} = action.payload;
      return {...state, purpose};
    }

    default:
      return state;
  }
}

export const getPurpose = (state: State) => state.purpose;
