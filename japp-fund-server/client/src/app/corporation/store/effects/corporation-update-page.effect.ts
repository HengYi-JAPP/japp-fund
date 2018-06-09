import {Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {of} from 'rxjs/observable/of';
import {catchError, combineLatest, exhaustMap, filter, map} from 'rxjs/operators';
import {corporationUpdatePageActions} from '..';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';
import {Corporation} from '../../../shared/models/corporation';

@Injectable()
export class CorporationUpdatePageEffect {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter(action => {
        const {event} = action.payload;
        return event.url.startsWith('/corporations/edit');
      }),
      combineLatest(this.route.queryParams),
      exhaustMap(a => {
        const [action, queryParams] = a;
        const {id} = queryParams;
        const corporation$ = id ? this.apiService.getCorporation(id) : of(new Corporation());
        return corporation$
          .pipe(
            map(corporation => new corporationUpdatePageActions.InitSuccess({corporation})),
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
