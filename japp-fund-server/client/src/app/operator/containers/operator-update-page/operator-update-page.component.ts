import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {finalize} from 'rxjs/operators';
import {Subscription} from 'rxjs/Subscription';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {OperatorGroup} from '../../../shared/models/operator-group';
import {operatorUpdatePageOperator} from '../../store';

@Component({
  selector: 'jfundsvr-operator-update-page',
  templateUrl: './operator-update-page.component.html',
  styleUrls: ['./operator-update-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OperatorUpdatePageComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-operator-update-page') b2 = true;
  private readonly _subscriptions: Subscription[] = [];
  readonly form: FormGroup;
  operator$: Observable<Operator>;
  readonly operatorGroups$: Observable<OperatorGroup[]>;
  readonly compareWithId = (o1, o2) => o1 && o2 && o1.id === o2.id;

  constructor(private store: Store<any>,
              private fb: FormBuilder,
              private router: Router,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.form = this.fb.group({
      'id': null,
      'admin': false,
      'name': [null, Validators.required],
      'operatorGroups': null,
      'permissions': null,
    });
    this.operatorGroups$ = this.apiService.listOperatorGroup();
    this.operator$ = this.store.select(operatorUpdatePageOperator);
    this._subscriptions.push(
      this.operator$.subscribe(it => this.form.patchValue(it)),
    );
  }

  submit() {
    console.log(this.form.value);
    this.apiService.updateOperator(this.id.value, this.form.value)
      .pipe(
        finalize(() => {
        }),
      )
      .subscribe(() => {
        this.router.navigate(['operators', 'manage']);
        this.utilService.showSuccess();
      }, err => this.utilService.showError(err));
  }

  get id() {
    return this.form.get('id');
  }

  get name() {
    return this.form.get('name');
  }

  get admin() {
    return this.form.get('admin');
  }

  get operatorGroups() {
    return this.form.get('operatorGroups');
  }

  get permissions() {
    return this.form.get('permissions');
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }

}
