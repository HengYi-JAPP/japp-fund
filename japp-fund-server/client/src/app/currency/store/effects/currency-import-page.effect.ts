import {of} from 'rxjs/observable/of';
import {Observable} from 'rxjs/Observable';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Action, Store} from '@ngrx/store';
import {exhaustMap} from 'rxjs/operators';
import {currencyUpdatePageActions, currencyImportPageActions} from '..';
import {Actions, Effect} from '@ngrx/effects';
import {MatDialog} from '@angular/material';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {Currency} from '../../../shared/models/currency';

@Injectable()
export class CurrencyImportPageEffect {
  // @Effect()
  // search$: Observable<Action> = this.actions$
  //   .ofType<currencyImportPageActions.Search>(currencyImportPageActions.SEARCH)
  //   .pipe(
  //     exhaustMap((id: string) => id ? this.apiService.get(id) : of({}))
  //   );

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private apiService: ApiService) {
  }
}
