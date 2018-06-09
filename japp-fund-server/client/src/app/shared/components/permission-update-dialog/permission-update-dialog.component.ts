import {ChangeDetectionStrategy, Component, HostBinding, Inject, OnDestroy, ViewChild} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef, MatSelectionList} from '@angular/material';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {forkJoin} from 'rxjs/observable/forkJoin';
import {of} from 'rxjs/observable/of';
import {Subscription} from 'rxjs/Subscription';
import {ApiService} from '../../../core/services/api.service';
import {DefaultCompare} from '../../../core/services/util.service';
import {Corporation} from '../../models/corporation';
import {Currency} from '../../models/currency';
import {Permission} from '../../models/permission';
import {ROLE_TYPES} from '../../services/permission-role-type-name.pipe';

@Component({
  selector: 'jfundsvr-permission-update-dialog',
  templateUrl: './permission-update-dialog.component.html',
  styleUrls: ['./permission-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PermissionUpdateDialogComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-dialog-comp') b1 = true;
  @HostBinding('class.jfundsvr-permission-update-dialog') b2 = true;
  @ViewChild('corporationSelectionList') corporationSelectionList: MatSelectionList;
  @ViewChild('currencySelectionList') currencySelectionList: MatSelectionList;
  private readonly _subscriptions: Subscription[] = [];
  readonly corporations$ = new BehaviorSubject<Corporation[]>([]);
  readonly currencies$ = new BehaviorSubject<Currency[]>([]);
  readonly roleTypes$ = of(ROLE_TYPES);
  readonly form: FormGroup;

  static open(dialog: MatDialog, data: { permission?: Permission }): MatDialogRef<PermissionUpdateDialogComponent, Permission> {
    data.permission = data.permission || new Permission();
    return dialog.open(PermissionUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  constructor(private fb: FormBuilder,
              private apiService: ApiService,
              private dialogRef: MatDialogRef<PermissionUpdateDialogComponent, Permission>,
              @Inject(MAT_DIALOG_DATA) private data: { permission: Permission }) {
    const {permission} = this.data;
    this.form = this.fb.group({
      'allCorporation': false,
      'corporationIds': [null, [Validators.required, Validators.minLength(1)]],
      'allCurrency': false,
      'currencyIds': [null, [Validators.required, Validators.minLength(1)]],
      'roleTypes': [null, [Validators.required, Validators.minLength(1)]],
    });
    this.form.patchValue(permission);
    // fixme form过早setValue，checkbox不更新
    const fun = () => {
      this.form.patchValue(permission);
      this._subscriptions.push(
        this.allCorporation.valueChanges.subscribe(it => {
          if (it) {
            this.corporationSelectionList.selectAll();
          } else {
            this.corporationSelectionList.deselectAll();
          }
        }),
        this.allCurrency.valueChanges.subscribe(it => {
          if (it) {
            this.currencySelectionList.selectAll();
          } else {
            this.currencySelectionList.deselectAll();
          }
        }),
      );
    };
    forkJoin(
      this.apiService.listCorporation(),
      this.apiService.listCurrency(),
    ).subscribe(a => {
      let [corporations, currencies] = a;
      corporations = (corporations || []).sort(DefaultCompare);
      this.corporations$.next(corporations);
      currencies = (currencies || []).sort(DefaultCompare);
      this.currencies$.next(currencies);
      // fixme form过早setValue，checkbox不更新
      setTimeout(() => fun());
    });
  }

  close() {
    const permission = this.form.value;
    if (this.allCorporation.value) {
      permission.corporationIds = [];
    }
    if (this.allCurrency.value) {
      permission.currencyIds = [];
    }
    this.dialogRef.close(permission);
  }

  get allCorporation() {
    return this.form.get('allCorporation');
  }

  get corporationIds() {
    return this.form.get('corporationIds');
  }

  get allCurrency() {
    return this.form.get('allCurrency');
  }

  get currencyIds() {
    return this.form.get('currencyIds');
  }

  get roleTypes() {
    return this.form.get('roleTypes');
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }
}
