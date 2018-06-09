import {of} from 'rxjs/observable/of';
import {Observable} from 'rxjs/Observable';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Action, Store} from '@ngrx/store';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';
import {operatorImportPageActions} from '../index';
import {Actions, Effect} from '@ngrx/effects';
import {MatDialog} from '@angular/material';
import {UtilService} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';

@Injectable()
export class OperatorImportPageEffect {
  //
  // @Effect()
  // create$: Observable<Action> = this.actions$
  //   .ofType(operatorImportPageActions.CREATE)
  //   .pluck('payload')
  //   .exhaustMap(
  //     (operator: Operator) => this.operatorService.save(operator),
  //     (payload: Operator, res: Operator) => {
  //       this.utilService.showSuccess();
  //       return new operatorImportPageActions.CreateSuccess(res);
  //     }
  //   ).catch(error => of(new ShowError(error)));

  constructor(private store: Store<any>,
              private actions$: Actions,
              private apiService: ApiService) {
  }
}
