import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {MatTabChangeEvent} from '@angular/material';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {map} from 'rxjs/operators';
import {Subscription} from 'rxjs/Subscription';
import {Corporation} from '../../../shared/models/corporation';
import {CorporationBalance} from '../../../shared/models/corporation-balance';
import {CDHP, Currency} from '../../../shared/models/currency';
import {
  corporationBalancePageBalances, corporationBalancePageCorporation, corporationBalancePageCurrencies,
  corporationBalancePageCurrency, corporationBalancePageDate
} from '../../store';

@Component({
  selector: 'jfundsvr-corporation-balance-page',
  templateUrl: './corporation-balance-page.component.html',
  styleUrls: ['./corporation-balance-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CorporationBalancePageComponent implements OnDestroy {
  private _subscriptions: Subscription[] = [];
  readonly corporationBalanceDataSource: CorporationBalanceDataSource;
  readonly corporationBalanceColumns = ['date', 'money'];
  corporation$: Observable<Corporation>;
  currencies$: Observable<Currency[]>;
  currencyCode$: Observable<string>;
  date$: Observable<Date>;

  constructor(private store: Store<any>,
              private route: ActivatedRoute) {
    this.corporationBalanceDataSource = new CorporationBalanceDataSource(store);
    this.corporation$ = this.store.select(corporationBalancePageCorporation);
    this.currencies$ = this.store.select(corporationBalancePageCurrencies);
    this.currencyCode$ = this.store.select(corporationBalancePageCurrency)
      .pipe(
        map(currency => currency.code === CDHP ? 'CNY' : currency.code),
      );
    this.date$ = this.store.select(corporationBalancePageDate);
  }

  tabChange(ev: MatTabChangeEvent) {
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }

}

class CorporationBalanceDataSource extends DataSource<CorporationBalance> {
  readonly data$: Observable<CorporationBalance[]>;

  constructor(private store: Store<any>) {
    super();
    this.data$ = this.store.select(corporationBalancePageBalances);
  }

  connect(collectionViewer: CollectionViewer): Observable<CorporationBalance[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
