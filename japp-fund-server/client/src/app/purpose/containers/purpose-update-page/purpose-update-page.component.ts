import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {finalize} from 'rxjs/operators';
import {Subscription} from 'rxjs/Subscription';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {Purpose} from '../../../shared/models/purpose';
import {purposeUpdatePagePurpose} from '../../store';

@Component({
  selector: 'jfundsvr-purpose-update-page',
  templateUrl: './purpose-update-page.component.html',
  styleUrls: ['./purpose-update-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PurposeUpdatePageComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-purpose-update-page') b2 = true;
  private readonly _subscriptions: Subscription[] = [];
  readonly form: FormGroup;
  readonly purpose$: Observable<Purpose>;

  constructor(private store: Store<any>,
              private fb: FormBuilder,
              private router: Router,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.form = this.fb.group({
      'id': null,
      'name': [null, Validators.required],
      'sortBy': [0, Validators.required],
    });
    this.purpose$ = this.store.select(purposeUpdatePagePurpose);
    this._subscriptions.push(
      this.purpose$.subscribe(it => this.form.patchValue(it)),
    );
  }

  submit() {
    console.log('start');
    this.apiService.savePurpose(this.form.value)
      .pipe(
        finalize(() => {
          console.log('end');
        }),
      )
      .subscribe(purpose => {
        this.router.navigate(['purposes', 'manage']);
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

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }

}
