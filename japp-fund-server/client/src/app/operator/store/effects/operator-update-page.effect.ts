import {Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {forkJoin} from 'rxjs/observable/forkJoin';
import {of} from 'rxjs/observable/of';
import {catchError, combineLatest, exhaustMap, filter, map} from 'rxjs/operators';
import {operatorUpdatePageActions} from '..';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';
import {Operator} from '../../../shared/models/operator';
import {OperatorPermission} from '../../../shared/models/operator-permission';

@Injectable()
export class OperatorUpdatePageEffect {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter(action => {
        const {event} = action.payload;
        return event.url.startsWith('/operators/edit');
      }),
      combineLatest(this.route.queryParams),
      exhaustMap(a => {
        const [action, queryParams] = a;
        const {id} = queryParams;
        return forkJoin(
          id ? this.apiService.getOperator(id) : of(new Operator()),
          id ? this.apiService.getOperator_Permission(id) : of(new OperatorPermission()),
        ).pipe(
          map(a1 => {
            const [operator, permission] = a1;
            Object.assign(operator, permission);
            return new operatorUpdatePageActions.InitSuccess({operator});
          }),
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
