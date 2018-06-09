// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {ActionReducer, MetaReducer} from '@ngrx/store';
import {HttpClient, HttpHeaders} from '@angular/common/http';

export const environment = {
  production: false,
};

export function TranslateLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

export const baseApiUrl = 'http://task.hengyi.com:8080/fund-server/api';

export const headers = new HttpHeaders({
  'Content-Type': 'application/json',
  'Accept': 'application/json',
  'Authorization': 'Bearer eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNqqViouTVKyUkpMyc3MU6oFAAAA__8.eERt_StmOPdJaRSm8ffuR25brkr8gwJo8UQjxrWd48zUtsL12gUZUflsk8tHoQ8L6lVdsdfrgpNTblALqodRsQ',
});

/**
 * By default, @ngrx/store uses combineReducers with the reducer map to compose
 * the root meta-reducer. To add more meta-reducers, provide an array of meta-reducers
 * that will be composed to form the root meta-reducer.
 */
export function logger(reducer: ActionReducer<any>): ActionReducer<any> {
  return function (state: any, action: any): any {
    console.log('state', state);
    console.log('action', action);

    return reducer(state, action);
  };
}

export const metaReducers: MetaReducer<any>[] = [logger];

