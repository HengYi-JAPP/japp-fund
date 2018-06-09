import {createSelector} from '@ngrx/store';
import * as moment from 'moment';
import {DefaultCompare} from '../../../core/services/util.service';
import {Corporation} from '../../../shared/models/corporation';
import {CorporationBalance} from '../../../shared/models/corporation-balance';
import {Currency} from '../../../shared/models/currency';
import {corporationBalancePageActions} from '../actions';

export interface State {
  corporation: Corporation;
  currencyId?: string;
  currencies: Currency[];
  balances: CorporationBalance[];
  year: number;
  month: number;
}

const initialState: State = {
  corporation: null,
  currencies: [],
  balances: [],
  year: new Date().getFullYear(),
  month: new Date().getMonth() + 1,
};

export function reducer(state = initialState, action: corporationBalancePageActions.Actions): State {
  switch (action.type) {
    case corporationBalancePageActions.SET_CORPORATION: {
      return {...state, corporation: action.payload};
    }

    case corporationBalancePageActions.SET_CURRENCIES: {
      return {...state, currencies: action.payload};
    }

    case corporationBalancePageActions.LIST_SUCCESS: {
      const {balances, year, month, date, currencyId} = action.payload;
      return {...state, balances, year, month, currencyId};
    }

    default:
      return state;
  }
}

export const getCorporation = (state: State) => state.corporation;
export const getCurrencyId = (state: State) => state.currencyId;
export const getCurrencies = (state: State) => state.currencies.sort((o1, o2) => DefaultCompare(o1, o2));
export const getCurrency = createSelector(getCurrencyId, getCurrencies, (id, array) => array.find(it => it.id === id));
export const getYear = (state: State) => state.year;
export const getMonth = (state: State) => state.month;
export const getDate = createSelector(getYear, getMonth, (year, month) => moment([year, month - 1, 1]).toDate());
export const getBalances = (state: State) => state.balances;
