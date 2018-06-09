import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy} from '@angular/core';
import {FormControl} from '@angular/forms';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {debounceTime, distinctUntilChanged, finalize, map, switchMap} from 'rxjs/operators';
import {Subscription} from 'rxjs/Subscription';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {OperatorSuggest} from '../../../shared/models/operator-suggest';
import {operatorImportPageActions, operatorImportPageOperatorSuggests} from '../../store';

@Component({
  selector: 'jfundsvr-operator-import-page',
  templateUrl: './operator-import-page.component.html',
  styleUrls: ['./operator-import-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OperatorImportPageComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-operator-import-page') b2 = true;
  private readonly _subscriptions: Subscription[] = [];
  readonly searchQCtrl = new FormControl();
  readonly operatorSuggestDataSource: DataSource<OperatorSuggest>;
  operatorSuggestColumns = ['oaId', 'hrId', 'name', 'btns'];

  constructor(private store: Store<any>,
              private router: Router,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.operatorSuggestDataSource = new OperatorSuggestDataSource(store);
    this._subscriptions.push(
      this.searchQCtrl.valueChanges
        .pipe(
          debounceTime(300),
          distinctUntilChanged(),
          switchMap(() => this.apiService.listOperatorSuggest(this.searchQCtrl.value)),
          map(operatorSuggests => new operatorImportPageActions.SearchSuccess({operatorSuggests})),
        )
        .subscribe(action => this.store.dispatch(action), err => this.utilService.showError(err)),
    );
  }

  create(operatorSuggest: OperatorSuggest) {
    this.apiService.createOperator(operatorSuggest)
      .pipe(
        finalize(() => {
        }),
      )
      .subscribe(operator => {
        this.router.navigate(['operators', 'edit'], {queryParams: {id: operator.id}});
        this.utilService.showSuccess();
      }, err => this.utilService.showError(err));
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }
}

class OperatorSuggestDataSource extends DataSource<OperatorSuggest> {
  data$: Observable<OperatorSuggest[]>;

  constructor(private store: Store<any>) {
    super();
    this.data$ = this.store.select(operatorImportPageOperatorSuggests);
  }

  connect(collectionViewer: CollectionViewer): Observable<OperatorSuggest[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }
}
