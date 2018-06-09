import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router} from '@angular/router';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';

@Injectable()
export class CorporationImportPageEffect {

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private apiService: ApiService) {
  }
}
