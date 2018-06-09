import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, forwardRef} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {MatDialog} from '@angular/material';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import {Permission} from '../../models/permission';
import {PermissionUpdateDialogComponent} from '../permission-update-dialog/permission-update-dialog.component';

@Component({
  selector: 'jfundsvr-permission-input',
  templateUrl: './permission-input.component.html',
  styleUrls: ['./permission-input.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => PermissionInputComponent),
    multi: true
  }],
})
export class PermissionInputComponent implements ControlValueAccessor {
  readonly permissionDataSource = new PermissionDataSource(new BehaviorSubject<Permission[]>([]));
  permissionColumns = ['corporations', 'currencies', 'roleTypes', 'btns'];

  constructor(private dialog: MatDialog) {
  }

  update(permission?: Permission) {
    PermissionUpdateDialogComponent.open(this.dialog, {permission})
      .afterClosed()
      .subscribe(res => {
        if (res) {
          let permissions = this.permissionDataSource.copy;
          const i = permissions.indexOf(permission);
          if (i >= 0) {
            permissions.splice(i, 1);
          }
          permissions = [res, ...permissions];
          this.handleChange(permissions);
        }
      });
  }

  delete(permission: Permission) {
    const permissions = this.permissionDataSource.copy.filter(it => it !== permission);
    this.handleChange(permissions);
  }

  handleChange(permissions: Permission[]): void {
    this.permissionDataSource.next(permissions);
    this.onModelChange(permissions);
  }

  writeValue(permissions: Permission[]): void {
    this.permissionDataSource.next(permissions);
  }

  onModelChange: Function = () => {
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  onModelTouched: Function = () => {
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }
}

class PermissionDataSource extends DataSource<Permission> {
  constructor(private data$: BehaviorSubject<Permission[]>) {
    super();
  }

  connect(collectionViewer: CollectionViewer): Observable<Permission[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

  next(permissions: Permission[]) {
    this.data$.next(permissions || []);
  }

  get copy(): Permission[] {
    const permissions = this.data$.value || [];
    return [...permissions];
  }

}
