import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ObservableMedia} from '@angular/flex-layout';
import {MatDialog, MatSnackBar, MatSnackBarConfig} from '@angular/material';
import {Store} from '@ngrx/store';
import {TranslateService} from '@ngx-translate/core';
import * as moment from 'moment';
import {Observable} from 'rxjs/Observable';
import {debounceTime, distinctUntilChanged, filter, map, startWith} from 'rxjs/operators';
import {isNullOrUndefined} from 'util';
import {ConfirmOption} from '../../shared/models/confirm-option';
import {ConfirmDialogComponent} from '../components/confirm-dialog/confirm-dialog.component';
import {coreActions} from '../store';

@Injectable()
export class UtilService {
  constructor(private http: HttpClient,
              private store: Store<any>,
              private translate: TranslateService,
              private media$: ObservableMedia,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
    media$.asObservable()
      .pipe(
        startWith(null),
        map(() => media$.isActive('xs')),
        debounceTime(300),
        distinctUntilChanged(),
        map(isMobile => new coreActions.SetIsMobileAction(isMobile)),
      )
      .subscribe(action => store.dispatch(action));
  }

  showSuccess(message?: string, action?: string, config?: MatSnackBarConfig) {
    config = config || {duration: 3000};
    message = message || 'TOAST.SUCCESS';
    this.translate.get(message).subscribe(res => this.snackBar.open(res, action, config));
  }

  showError(err: any) {
    console.log(err);
  }

  showConfirm(option?: ConfirmOption): Observable<any> {
    const data = Object.assign({
      textContent: 'COMMON.DELETE_CONFIRM',
      okText: 'COMMON.CONFIRM',
      cancelText: 'COMMON.CANCEL'
    }, option);
    return this.dialog.open(ConfirmDialogComponent, {data})
      .afterClosed()
      .pipe(
        filter(res => res)
      );
  }
}

export const DefaultValue = (v: any, d: any): any => {
  if (isNullOrUndefined(v)) {
    return d;
  }
  return v;
};

export const CheckQ = (sV: string, qV: string): boolean => {
  let s = sV || '';
  s = s.toLocaleLowerCase();
  if (qV) {
    const q = qV.toLocaleLowerCase();
    const b = s.indexOf(q) > -1;
    if (!b) {
      return false;
    }
  }
  return true;
};

export const DefaultCompare = (o1: any, o2: any): number => {
  if (o1.id === '0') {
    return -1;
  }
  if (o2.id === '0') {
    return 1;
  }
  return o2.sortBy - o1.sortBy;
};

export const toDate = (v: any): Date => {
  return v && moment(v).toDate();
};

export const isWeixinBrowser = (): boolean => {
  return /micromessenger/i.test(navigator.userAgent);
};

export const isIos = (): boolean => {
  return !!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
};
