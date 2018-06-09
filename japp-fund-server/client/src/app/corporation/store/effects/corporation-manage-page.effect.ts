import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {of} from 'rxjs/observable/of';
import {catchError, filter, map, switchMap} from 'rxjs/operators';
import {corporationManagePageActions} from '..';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';

@Injectable()
export class CorporationManagePageEffect {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter(action => {
        const {event} = action.payload;
        return event.url.startsWith('/corporations/manage');
      }),
      switchMap(() => {
        return this.apiService.listCorporation()
          .pipe(
            map(corporations => new corporationManagePageActions.InitSuccess({corporations})),
            catchError(error => of(new ShowError(error)))
          );
      }),
    );

  constructor(private actions$: Actions,
              private apiService: ApiService) {
  }
}
