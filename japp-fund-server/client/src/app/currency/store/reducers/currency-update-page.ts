import {Currency} from '../../../shared/models/currency';
import {currencyUpdatePageActions} from '../actions';

export interface State {
  currency: Currency;
}

const initialState: State = {
  currency: null,
};

export function reducer(state = initialState, action: currencyUpdatePageActions.Actions): State {
  switch (action.type) {
    case currencyUpdatePageActions.INIT_SUCCESS: {
      const {currency} = action.payload;
      return {...state, currency};
    }

    default:
      return state;
  }
}

export const getCurrency = (state: State) => state.currency;
