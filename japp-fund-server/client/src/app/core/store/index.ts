import {createSelector, createFeatureSelector} from '@ngrx/store';

export * from './actions';
import * as fromCommon from './reducers/common';

export interface CoreState {
  common: fromCommon.State;
}

export interface State {
  core: CoreState;
}

export const featureName = 'core';

export const reducers = {
  common: fromCommon.reducer,
};

export const selectCoreState = createFeatureSelector<CoreState>(featureName);

/**
 * Layout Reducers
 */
export const selectCommonState = createSelector(selectCoreState, (state: CoreState) => state.common);
export const getShowSidenav = createSelector(selectCommonState, fromCommon.getShowSidenav);
export const getIsMobile = createSelector(selectCommonState, fromCommon.getIsMobile);
