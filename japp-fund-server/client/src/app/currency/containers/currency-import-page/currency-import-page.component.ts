import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, HostBinding} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {CurrencySuggest} from '../../../shared/models/currency-suggest';
import {currencyImportPageCurrencySuggests} from '../../store';

@Component({
  selector: 'jfundsvr-currency-import-page',
  templateUrl: './currency-import-page.component.html',
  styleUrls: ['./currency-import-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurrencyImportPageComponent {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-currency-import-page') b2 = true;
  readonly currencySuggestDataSource: DataSource<CurrencySuggest>;
  readonly currencySuggestColumns = ['code', 'name', 'btns'];

  constructor(private store: Store<any>,
              private utilService: UtilService) {
    this.currencySuggestDataSource = new CurrencySuggestDataSource(store);
  }

}

class CurrencySuggestDataSource extends DataSource<CurrencySuggest> {
  data$: Observable<CurrencySuggest[]>;

  constructor(private store: Store<any>) {
    super();
    this.data$ = this.store.select(currencyImportPageCurrencySuggests);
  }

  connect(collectionViewer: CollectionViewer): Observable<CurrencySuggest[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
