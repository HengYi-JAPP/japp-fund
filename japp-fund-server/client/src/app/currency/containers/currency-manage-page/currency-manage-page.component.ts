import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, HostBinding} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {finalize, switchMap} from 'rxjs/operators';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {Currency} from '../../../shared/models/currency';
import {currencyManagePageActions, currencyManagePageCurrencies} from '../../store';

@Component({
  selector: 'jfundsvr-currency-manage-page',
  templateUrl: './currency-manage-page.component.html',
  styleUrls: ['./currency-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurrencyManagePageComponent {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-currency-manage-page') b2 = true;
  currencyDataSource: DataSource<Currency>;
  currencyColumns = ['id', 'code', 'name', 'sortBy', 'btns'];

  constructor(private store: Store<any>,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.currencyDataSource = new CurrencyDataSource(store);
  }

  delete(currency: Currency) {
    this.utilService.showConfirm()
      .pipe(
        switchMap(() => this.apiService.deleteCurrency(currency.id)),
        finalize(() => console.log('test', 'finalize')),
      )
      .subscribe(() => {
        const action = new currencyManagePageActions.DeleteSuccess({id: currency.id});
        this.store.dispatch(action);
        this.utilService.showSuccess();
      }, error => this.utilService.showError(error));
  }

}

class CurrencyDataSource extends DataSource<Currency> {
  data$: Observable<Currency[]>;

  constructor(private store: Store<any>) {
    super();
    this.data$ = this.store.select(currencyManagePageCurrencies);
  }

  connect(collectionViewer: CollectionViewer): Observable<Currency[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
