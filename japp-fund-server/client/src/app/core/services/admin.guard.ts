import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {catchError, map} from 'rxjs/operators';
import {ApiService} from './api.service';
import {UtilService} from './util.service';

@Injectable()
export class AdminGuard implements CanActivate {

  constructor(private store: Store<any>,
              private utilService: UtilService,
              private apiService: ApiService) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.apiService.getAuth()
      .pipe(
        map(it => it.admin),
        catchError(() => {
          this.utilService.showError('err');
          return of(false);
        })
      );
  }

}
