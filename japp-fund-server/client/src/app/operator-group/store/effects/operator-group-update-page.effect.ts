import {Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {of} from 'rxjs/observable/of';
import {catchError, combineLatest, exhaustMap, filter, map} from 'rxjs/operators';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';
import {OperatorGroup} from '../../../shared/models/operator-group';
import {operatorGroupUpdatePageActions} from '../index';

@Injectable()
export class OperatorGroupUpdatePageEffect {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter(action => {
        const {event} = action.payload;
        return event.url.startsWith('/operatorGroups/edit');
      }),
      combineLatest(this.route.queryParams),
      exhaustMap(a => {
        const [action, queryParams] = a;
        const {id} = queryParams;
        const operatorGroup$ = id ? this.apiService.getOperatorGroup(id) : of(new OperatorGroup());
        return operatorGroup$
          .pipe(
            map(operatorGroup => new operatorGroupUpdatePageActions.InitSuccess({operatorGroup})),
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
