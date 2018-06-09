import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {finalize} from 'rxjs/operators';
import {Subscription} from 'rxjs/Subscription';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {Corporation} from '../../../shared/models/corporation';
import {corporationUpdatePageCorporation} from '../../store';

@Component({
  selector: 'jfundsvr-corporation-update-page',
  templateUrl: './corporation-update-page.component.html',
  styleUrls: ['./corporation-update-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CorporationUpdatePageComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-corporation-update-page') b2 = true;
  private readonly _subscriptions: Subscription[] = [];
  readonly form: FormGroup;
  readonly corporation$: Observable<Corporation>;

  constructor(private store: Store<any>,
              private fb: FormBuilder,
              private router: Router,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.form = this.fb.group({
      'id': [null, Validators.required],
      'code': [null, Validators.required],
      'name': [null, Validators.required],
      'sortBy': [0, Validators.required],
    });
    this.corporation$ = this.store.select(corporationUpdatePageCorporation);
    this._subscriptions.push(
      this.corporation$.subscribe(it => this.form.patchValue(it)),
    );
  }

  submit() {
    this.apiService.updateCorporation(this.id.value, this.form.value)
      .pipe(
        finalize(() => {
        }),
      )
      .subscribe(purpose => {
        this.router.navigate(['corporations', 'manage']);
        this.utilService.showSuccess();
      }, err => {
        this.utilService.showError(err);
      });
  }

  get id() {
    return this.form.get('id');
  }

  get code() {
    return this.form.get('code');
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
