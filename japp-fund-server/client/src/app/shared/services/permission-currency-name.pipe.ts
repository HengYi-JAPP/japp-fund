import {Pipe, PipeTransform} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {from} from 'rxjs/observable/from';
import {of} from 'rxjs/observable/of';
import {map, mergeMap, toArray} from 'rxjs/operators';
import {ApiService} from '../../core/services/api.service';
import {DefaultCompare} from '../../core/services/util.service';
import {Permission} from '../models/permission';

@Pipe({
  name: 'permissionCurrencyName'
})
export class PermissionCurrencyNamePipe implements PipeTransform {
  constructor(private apiService: ApiService) {
  }

  transform(permission: Permission, args?: any): Observable<string> {
    if (!permission) {
      return of('');
    }
    if (permission.allCurrency) {
      return of('*');
    }
    return from(permission.currencyIds)
      .pipe(
        mergeMap(id => this.apiService.getCurrency(id)),
        toArray(),
        map(currencies => currencies
          .sort(DefaultCompare)
          .map(it => it.name)
          .join(',')
        ),
      );
  }

}
