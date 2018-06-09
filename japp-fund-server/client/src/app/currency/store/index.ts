import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as importPage from './reducers/currency-import-page';
import * as managePage from './reducers/currency-manage-page';
import * as updatePage from './reducers/currency-update-page';

export * from './actions';

export interface CurrencyState {
  managePage: managePage.State;
  updatePage: updatePage.State;
  importPage: importPage.State;
}

export const reducers = {
  managePage: managePage.reducer,
  updatePage: updatePage.reducer,
  importPage: importPage.reducer,
};

export const featureName = 'currency';
export const moduleState = createFeatureSelector<CurrencyState>(featureName);
export const currencyManagePageState = createSelector(moduleState, (state: CurrencyState) => state.managePage);
export const currencyUpdatePageState = createSelector(moduleState, (state: CurrencyState) => state.updatePage);
export const currencyImportPageState = createSelector(moduleState, (state: CurrencyState) => state.importPage);

export const currencyManagePageCurrencies = createSelector(currencyManagePageState, managePage.getCurrencies);

export const currencyUpdatePageCurrency = createSelector(currencyUpdatePageState, updatePage.getCurrency);

export const currencyImportPageCurrencySuggests = createSelector(currencyImportPageState, importPage.corporationSuggests);
