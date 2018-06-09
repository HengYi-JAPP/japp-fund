import {Action} from '@ngrx/store';
import {Authenticate, AuthenticateResponse} from '../../../shared/models/authenticate';

export const LOGIN_REDIRECT = '[Core] LOGIN_REDIRECT';

export class LoginRedirect implements Action {
  readonly type = LOGIN_REDIRECT;
}

export const LOGIN = '[Auth] LOGIN';

export class Login implements Action {
  readonly type = LOGIN;

  constructor(public payload: Authenticate) {
  }
}

export const LOGIN_SUCCESS = '[Auth] LOGIN_SUCCESS';

export class LoginSuccess implements Action {
  readonly type = LOGIN_SUCCESS;

  constructor(public payload: AuthenticateResponse) {
  }
}

export const LOGOUT = '[Auth] LOGOUT';


export class Logout implements Action {
  readonly type = LOGOUT;
}

export const SHOW_ERROR = '[Core] SHOW_ERROR';

export class ShowError implements Action {
  readonly type = SHOW_ERROR;

  constructor(public payload: any) {
  }
}

export const OPEN_SIDENAV = '[Core] OPEN_SIDENAV';

export class OpenSidenav implements Action {
  readonly type = OPEN_SIDENAV;
}

export const CLOSE_SIDENAV = '[Core] CLOSE_SIDENAV';

export class CloseSidenav implements Action {
  readonly type = CLOSE_SIDENAV;
}

export const SET_IS_MOBILE = '[Core] SET_IS_MOBILE';

export class SetIsMobileAction implements Action {
  readonly type = SET_IS_MOBILE;

  constructor(public payload: boolean) {
  }
}

export type Actions =
  | Login
  | LoginSuccess
  | LoginRedirect
  | Logout
  | ShowError
  | OpenSidenav
  | CloseSidenav
  | SetIsMobileAction;
