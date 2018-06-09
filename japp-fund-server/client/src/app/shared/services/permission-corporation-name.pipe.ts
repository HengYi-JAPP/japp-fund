import {Pipe, PipeTransform} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {from} from 'rxjs/observable/from';
import {of} from 'rxjs/observable/of';
import {map, mergeMap, toArray} from 'rxjs/operators';
import {ApiService} from '../../core/services/api.service';
import {DefaultCompare} from '../../core/services/util.service';
import {Permission} from '../models/permission';

@Pipe({
  name: 'permissionCorporationName'
})
export class PermissionCorporationNamePipe implements PipeTransform {
  constructor(private apiService: ApiService) {
  }

  transform(permission: Permission, args?: any): Observable<string> {
    if (!permission) {
      return of('');
    }
    if (permission.allCorporation) {
      return of('*');
    }
    return from(permission.corporationIds)
      .pipe(
        mergeMap(id => this.apiService.getCorporation(id)),
        toArray(),
        map(corporations => corporations
          .sort(DefaultCompare)
          .map(it => it.name)
          .join(',')
        ),
      );
  }

}
