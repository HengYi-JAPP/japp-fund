import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {MetaReducer} from '@ngrx/store';

export const environment = {
  production: true,
};

export function TranslateLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, '/fund-server/assets/i18n/');
}

export const baseApiUrl = 'http://task.hengyi.com:8080/fund-server/api';

export const headers = new HttpHeaders({
  'Content-Type': 'application/json',
  'Accept': 'application/json',
  'Authorization': 'Bearer eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNqqViouTVKyUkpMyc3MU6oFAAAA__8.eERt_StmOPdJaRSm8ffuR25brkr8gwJo8UQjxrWd48zUtsL12gUZUflsk8tHoQ8L6lVdsdfrgpNTblALqodRsQ',
});

export const metaReducers: MetaReducer<any>[] = [];

