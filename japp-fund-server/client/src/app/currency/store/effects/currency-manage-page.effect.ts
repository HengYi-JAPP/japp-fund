import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {of} from 'rxjs/observable/of';
import {catchError, exhaustMap, filter, map} from 'rxjs/operators';
import {currencyManagePageActions} from '..';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';

@Injectable()
export class CurrencyManagePageEffect {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter(action => {
        const {event} = action.payload;
        return event.url.startsWith('/currencies/manage');
      }),
      exhaustMap(() => {
        return this.apiService.listCurrency()
          .pipe(
            map(currencies => new currencyManagePageActions.InitSuccess({currencies})),
            catchError(err => of(new ShowError(err)))
          );
      }),
    );

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private utilService: UtilService,
              private apiService: ApiService) {
  }
}
