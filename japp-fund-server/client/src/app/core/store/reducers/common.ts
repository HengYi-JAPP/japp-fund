import * as Core from '../actions/core';

export interface State {
  showSidenav: boolean;
  isMobile: boolean;
}

const initialState: State = {
  showSidenav: true,
  isMobile: false,
};

export function reducer(state = initialState, action: Core.Actions): State {
  switch (action.type) {
    case Core.CLOSE_SIDENAV: {
      return {
        ...state,
        showSidenav: false,
      };
    }

    case Core.OPEN_SIDENAV: {
      return {
        ...state,
        showSidenav: true,
      };
    }

    case Core.SET_IS_MOBILE: {
      const isMobile = action.payload;
      return {
        ...state,
        isMobile,
        showSidenav: !isMobile,
      };
    }

    default:
      return state;
  }
}

export const getShowSidenav = (state: State) => state.showSidenav;
export const getIsMobile = (state: State) => state.isMobile;
