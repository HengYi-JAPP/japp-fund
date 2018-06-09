import {HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {ActivatedRoute, Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import * as moment from 'moment';
import {of} from 'rxjs/observable/of';
import {catchError, combineLatest, filter, map, switchMap} from 'rxjs/operators';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {corporationBalancePageActions} from '../index';

@Injectable()
export class CorporationBalancePageEffect {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter(action => {
        const {event} = action.payload;
        return event.url.startsWith('/corporations/balance');
      }),
      combineLatest(this.route.queryParams),
      switchMap(a => {
        const [action, queryParams] = a;
        let {corporationId, currencyId, year, month} = queryParams;
        corporationId = corporationId || 'L4jWnLywCoTXbaLbsZvDPU';
        currencyId = currencyId || 'g111W2qibFUfXSub6MEEJ';
        year = year || moment().year();
        month = month || (moment().month() + 1);
        const params = new HttpParams()
          .set('corporationId', corporationId)
          .set('currencyId', currencyId)
          .set('year', year)
          .set('month', month);
        return this.apiService.listCorporationBalance(params)
          .pipe(
            map(balances => new corporationBalancePageActions.ListSuccess({
              balances,
              corporationId,
              currencyId,
              year,
              month
            })),
            catchError(error => of(new ShowError(error)))
          );
      }),
    );

  constructor(private store: Store<any>,
              private actions$: Actions,
              private route: ActivatedRoute,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private apiService: ApiService) {
  }
}
