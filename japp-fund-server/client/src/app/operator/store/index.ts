import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as importPage from './reducers/operator-import-page';
import * as managePage from './reducers/operator-manage-page';
import * as updatePage from './reducers/operator-update-page';

export * from './actions';

export interface OperatorState {
  managePage: managePage.State;
  updatePage: updatePage.State;
  importPage: importPage.State;
}

export const reducers = {
  managePage: managePage.reducer,
  updatePage: updatePage.reducer,
  importPage: importPage.reducer,
};

export const featureName = 'operator';
export const operatorState = createFeatureSelector<OperatorState>(featureName);
export const managePageState = createSelector(operatorState, (state: OperatorState) => state.managePage);
export const updatePageState = createSelector(operatorState, (state: OperatorState) => state.updatePage);
export const importPageState = createSelector(operatorState, (state: OperatorState) => state.importPage);

export const operatorManagePageOperators = createSelector(managePageState, managePage.getOperators);

export const operatorUpdatePageOperator = createSelector(updatePageState, updatePage.getOperator);

export const operatorImportPageOperatorSuggests = createSelector(importPageState, importPage.getOperatorSuggests);
