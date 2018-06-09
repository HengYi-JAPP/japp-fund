import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {of} from 'rxjs/observable/of';
import {catchError, exhaustMap, filter, map} from 'rxjs/operators';
import {purposeManagePageActions} from '..';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';

@Injectable()
export class PurposeManagePageEffect {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter(action => {
        const {event} = action.payload;
        return event.url.startsWith('/purposes/manage');
      }),
      exhaustMap(() => {
        return this.apiService.listPurpose()
          .pipe(
            map(purposes => new purposeManagePageActions.InitSuccess({purposes})),
            catchError(err => of(new ShowError(err)))
          );
      }),
    );

  constructor(private store: Store<any>,
              private actions$: Actions,
              private apiService: ApiService) {
  }
}
