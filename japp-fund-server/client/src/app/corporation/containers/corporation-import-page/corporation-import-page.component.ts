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
import {CorporationSuggest} from '../../../shared/models/corporation-suggest';
import {corporationImportPageActions, corporationImportPageCorporationSuggests} from '../../store';

@Component({
  selector: 'jfundsvr-corporation-import-page',
  templateUrl: './corporation-import-page.component.html',
  styleUrls: ['./corporation-import-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CorporationImportPageComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-corporation-import-page') b2 = true;
  private readonly _subscriptions: Subscription[] = [];
  readonly searchQCtrl = new FormControl();
  readonly corporationSuggestDataSource: DataSource<CorporationSuggest>;
  corporationSuggestColumns = ['code', 'name', 'btns'];

  constructor(private store: Store<any>,
              private router: Router,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.corporationSuggestDataSource = new CorporationSuggestDataSource(store);
    this._subscriptions.push(
      this.searchQCtrl.valueChanges
        .pipe(
          debounceTime(300),
          distinctUntilChanged(),
          switchMap(() => this.apiService.listCorporationSuggest(this.searchQCtrl.value)),
          map(corporationSuggests => new corporationImportPageActions.SearchSuccess({corporationSuggests})),
        )
        .subscribe(action => this.store.dispatch(action), err => this.utilService.showError(err)),
    );
  }

  create(corporationSuggest: CorporationSuggest) {
    this.apiService.createCorporation(corporationSuggest)
      .pipe(
        finalize(() => {
        }),
      )
      .subscribe(() => {
        this.router.navigate(['corporations', 'manage']);
        this.utilService.showSuccess();
      }, err => {
        this.utilService.showError(err);
      });
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }

}

class CorporationSuggestDataSource extends DataSource<CorporationSuggest> {
  data$: Observable<CorporationSuggest[]>;

  constructor(private store: Store<any>) {
    super();
    this.data$ = this.store.select(corporationImportPageCorporationSuggests);
  }

  connect(collectionViewer: CollectionViewer): Observable<CorporationSuggest[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
