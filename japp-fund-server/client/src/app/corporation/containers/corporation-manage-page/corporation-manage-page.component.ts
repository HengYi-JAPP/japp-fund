import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy} from '@angular/core';
import {FormControl} from '@angular/forms';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {debounceTime, distinctUntilChanged, map} from 'rxjs/operators';
import {Subscription} from 'rxjs/Subscription';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {Corporation} from '../../../shared/models/corporation';
import {corporationManagePageActions, corporationManagePageCorporations} from '../../store';

@Component({
  selector: 'jfundsvr-corporation-manage-page',
  templateUrl: './corporation-manage-page.component.html',
  styleUrls: ['./corporation-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CorporationManagePageComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-corporation-manage-page') b2 = true;
  private readonly _subscriptions: Subscription[] = [];
  readonly filterNameQCtrl = new FormControl();
  readonly corporationDataSource: CorporationDataSource;
  readonly corporationColumns = ['id', 'code', 'name', 'sortBy', 'btns'];

  constructor(private store: Store<any>,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.corporationDataSource = new CorporationDataSource(store);
    this._subscriptions.push(
      this.filterNameQCtrl.valueChanges
        .pipe(
          debounceTime(300),
          distinctUntilChanged(),
          map(it => new corporationManagePageActions.SetFilterNameQ(it))
        )
        .subscribe(action => this.store.dispatch(action)),
    );
  }

  delete(corporation: Corporation) {
    this.utilService.showConfirm()
      .subscribe(() => {

      });
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }

}

class CorporationDataSource extends DataSource<Corporation> {
  data$: Observable<Corporation[]>;

  constructor(private store: Store<any>) {
    super();
    this.data$ = this.store.select(corporationManagePageCorporations);
  }

  connect(collectionViewer: CollectionViewer): Observable<Corporation[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
