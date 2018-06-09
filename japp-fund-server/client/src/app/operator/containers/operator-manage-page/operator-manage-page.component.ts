import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy} from '@angular/core';
import {FormControl} from '@angular/forms';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {debounceTime, distinctUntilChanged, map} from 'rxjs/operators';
import {Subscription} from 'rxjs/Subscription';
import {UtilService} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {operatorManagePageActions, operatorManagePageOperators} from '../../store';

@Component({
  selector: 'jfundsvr-operator-manage-page',
  templateUrl: './operator-manage-page.component.html',
  styleUrls: ['./operator-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OperatorManagePageComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-operator-manage-page') b2 = true;
  private readonly _subscriptions: Subscription[] = [];
  readonly filterNameQCtrl = new FormControl();
  readonly operatorDataSource: DataSource<Operator>;
  operatorColumns = ['id', 'name', 'admin', 'btns'];

  constructor(private store: Store<any>,
              private utilService: UtilService) {
    this.operatorDataSource = new OperatorDataSource(store);
    this._subscriptions.push(
      this.filterNameQCtrl.valueChanges
        .pipe(
          debounceTime(300),
          distinctUntilChanged(),
          map(it => new operatorManagePageActions.SetFilterNameQ(it))
        )
        .subscribe(action => this.store.dispatch(action)),
    );
  }

  delete(operator: Operator) {
    this.utilService.showConfirm()
      .subscribe(() => {

      });
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }
}

class OperatorDataSource extends DataSource<Operator> {
  data$: Observable<Operator[]>;

  constructor(private store: Store<any>) {
    super();
    this.data$ = this.store.select(operatorManagePageOperators);
  }

  connect(collectionViewer: CollectionViewer): Observable<Operator[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
