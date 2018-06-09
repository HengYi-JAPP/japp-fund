import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as managePage from './reducers/purpose-manage-page';
import * as updatePage from './reducers/purpose-update-page';

export * from './actions';

export interface PurposeState {
  managePage: managePage.State;
  updatePage: updatePage.State;
}

export const reducers = {
  managePage: managePage.reducer,
  updatePage: updatePage.reducer,
};

export const featureName = 'purpose';
export const purposeState = createFeatureSelector<PurposeState>(featureName);
export const managePageState = createSelector(purposeState, (state: PurposeState) => state.managePage);
export const updatePageState = createSelector(purposeState, (state: PurposeState) => state.updatePage);

export const purposeManagePagePurposes = createSelector(managePageState, managePage.getPurposes);

export const purposeUpdatePagePurpose = createSelector(updatePageState, updatePage.getPurpose);
