import {Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {of} from 'rxjs/observable/of';
import {catchError, combineLatest, exhaustMap, filter, map} from 'rxjs/operators';
import {currencyUpdatePageActions} from '..';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';
import {Currency} from '../../../shared/models/currency';

@Injectable()
export class CurrencyUpdatePageEffect {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter(action => {
        const {event} = action.payload;
        return event.url.startsWith('/currencies/edit');
      }),
      combineLatest(this.route.queryParams),
      exhaustMap(a => {
        const [action, queryParams] = a;
        const {id} = queryParams;
        const currency$ = id ? this.apiService.getCurrency(id) : of(new Currency());
        return currency$
          .pipe(
            map(currency => new currencyUpdatePageActions.InitSuccess({currency})),
            catchError(err => of(new ShowError(err)))
          );
      }),
    );

  constructor(private store: Store<any>,
              private actions$: Actions,
              private route: ActivatedRoute,
              private apiService: ApiService) {
  }
}
