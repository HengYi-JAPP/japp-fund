import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as balancePage from './reducers/corporation-balance-page';
import * as importPage from './reducers/corporation-import-page';
import * as managePage from './reducers/corporation-manage-page';
import * as updatePage from './reducers/corporation-update-page';

export * from './actions';

interface CorporationState {
  managePage: managePage.State;
  updatePage: updatePage.State;
  importPage: importPage.State;
  balancePage: balancePage.State;
}

export const reducers = {
  managePage: managePage.reducer,
  updatePage: updatePage.reducer,
  importPage: importPage.reducer,
  balancePage: balancePage.reducer,
};

export const featureName = 'corporation';
const corporationState = createFeatureSelector<CorporationState>(featureName);
const managePageState = createSelector(corporationState, (state: CorporationState) => state.managePage);
const updatePageState = createSelector(corporationState, (state: CorporationState) => state.updatePage);
const importPageState = createSelector(corporationState, (state: CorporationState) => state.importPage);
const balancePageState = createSelector(corporationState, (state: CorporationState) => state.balancePage);

export const corporationManagePageCorporations = createSelector(managePageState, managePage.getCorporations);

export const corporationUpdatePageCorporation = createSelector(updatePageState, updatePage.getCorporation);

export const corporationImportPageCorporationSuggests = createSelector(importPageState, importPage.getCorporationSuggests);

export const corporationBalancePageCorporation = createSelector(balancePageState, balancePage.getCorporation);
export const corporationBalancePageCurrencies = createSelector(balancePageState, balancePage.getCurrencies);
export const corporationBalancePageCurrency = createSelector(balancePageState, balancePage.getCurrency);
export const corporationBalancePageDate = createSelector(balancePageState, balancePage.getDate);
export const corporationBalancePageBalances = createSelector(balancePageState, balancePage.getBalances);
