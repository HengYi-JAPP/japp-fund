import {Pipe, PipeTransform} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Observable} from 'rxjs/Observable';
import {from} from 'rxjs/observable/from';
import {of} from 'rxjs/observable/of';
import {concatMap, map, toArray} from 'rxjs/operators';
import {Permission} from '../models/permission';

export const ROLE_TYPES = ['MONTH_PLAN_UPDATE', 'DAY_PLAN_UPDATE', 'FUND_UPDATE', 'FUND_EAD'];
export const DEFAULT_ROLE_TYPES = ['FUND_EAD'];

@Pipe({
  name: 'permissionRoleTypeName'
})
export class PermissionRoleTypeNamePipe implements PipeTransform {
  constructor(private translate: TranslateService) {
  }

  static getTName(roleType: string): string {
    return 'PERMISSION.ROLETYPE-' + roleType;
  }

  transform(permission: Permission, args?: any): Observable<string> {
    if (!permission || !permission.roleTypes || permission.roleTypes.length === 0) {
      return of('');
    }
    if (permission.roleTypes.length === 4) {
      return of('*');
    }
    return from(permission.roleTypes)
      .pipe(
        map(PermissionRoleTypeNamePipe.getTName),
        concatMap(it => this.translate.get(it)),
        toArray(),
        map(names => names.join(',')),
      );
  }

}
