import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as managePage from './reducers/operator-group-manage-page';
import * as updatePage from './reducers/operator-group-update-page';

export * from './actions';

export interface OperatorGroupState {
  managePage: managePage.State;
  updatePage: updatePage.State;
}

export const reducers = {
  managePage: managePage.reducer,
  updatePage: updatePage.reducer,
};

export const featureName = 'operatorGroup';
const operatorGroupState = createFeatureSelector<OperatorGroupState>(featureName);
const managePageState = createSelector(operatorGroupState, (state: OperatorGroupState) => state.managePage);
const updatePageState = createSelector(operatorGroupState, (state: OperatorGroupState) => state.updatePage);

export const operatorGroupManagePageOperatorGroups = createSelector(managePageState, managePage.getOperatorGroups);

export const operatorGroupUpdatePageOperatorGroup = createSelector(updatePageState, updatePage.getOperatorGroup);
