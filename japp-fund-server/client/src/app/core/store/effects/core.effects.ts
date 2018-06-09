import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {concatMap} from 'rxjs/operators';
import {ApiService} from '../../services/api.service';
import {UtilService} from '../../services/util.service';
import {coreActions} from '../actions';

@Injectable()
export class CoreEffects {
  @Effect()
  showError$: Observable<Action> = this.actions$
    .ofType<coreActions.ShowError>(coreActions.SHOW_ERROR)
    .pipe(
      concatMap(action => {
        this.utilService.showError(action.payload);
        return of();
      })
    );

  constructor(private actions$: Actions,
              private router: Router,
              private apiService: ApiService,
              private utilService: UtilService) {
  }

}
