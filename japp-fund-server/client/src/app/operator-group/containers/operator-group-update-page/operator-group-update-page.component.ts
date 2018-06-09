import {Component, HostBinding, OnDestroy} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {finalize} from 'rxjs/operators';
import {Subscription} from 'rxjs/Subscription';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {OperatorGroup} from '../../../shared/models/operator-group';
import {operatorGroupUpdatePageOperatorGroup} from '../../store';

@Component({
  selector: 'jfundsvr-operator-group-update-page',
  templateUrl: './operator-group-update-page.component.html',
  styleUrls: ['./operator-group-update-page.component.less']
})
export class OperatorGroupUpdatePageComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-currency-update-page') b2 = true;
  private readonly _subscriptions: Subscription[] = [];
  readonly form: FormGroup;
  readonly operatorGroup$: Observable<OperatorGroup>;

  constructor(private store: Store<any>,
              private fb: FormBuilder,
              private router: Router,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.form = this.fb.group({
      'id': null,
      'name': [null, Validators.required],
      'sortBy': [0, Validators.required],
      'permissions': [null, [Validators.required, Validators.minLength(1)]],
    });
    this.operatorGroup$ = this.store.select(operatorGroupUpdatePageOperatorGroup);
    this._subscriptions.push(
      this.operatorGroup$.subscribe(it => this.form.patchValue(it)),
    );
  }

  submit() {
    this.apiService.saveOperatorGroup(this.form.value)
      .pipe(
        finalize(() => {
          console.log('end');
        }),
      )
      .subscribe(purpose => {
        this.router.navigate(['operatorGroups', 'manage']);
        this.utilService.showSuccess();
      }, err => {
        this.utilService.showError(err);
      });
  }

  get name() {
    return this.form.get('name');
  }

  get sortBy() {
    return this.form.get('sortBy');
  }

  get permissions() {
    return this.form.get('permissions');
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }

}
