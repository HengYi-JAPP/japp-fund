import {Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {of} from 'rxjs/observable/of';
import {catchError, combineLatest, exhaustMap, filter, map} from 'rxjs/operators';
import {purposeUpdatePageActions} from '..';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';
import {Purpose} from '../../../shared/models/purpose';

@Injectable()
export class PurposeUpdatePageEffect {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter(action => {
        const {event} = action.payload;
        return event.url.startsWith('/purposes/edit');
      }),
      combineLatest(this.route.queryParams),
      exhaustMap(a => {
        const [action, queryParams] = a;
        const {id} = queryParams;
        const purpose$ = id ? this.apiService.getPurpose(id) : of(new Purpose());
        return purpose$
          .pipe(
            map(purpose => new purposeUpdatePageActions.InitSuccess({purpose})),
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
