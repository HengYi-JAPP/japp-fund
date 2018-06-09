import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, HostBinding} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {finalize, switchMap} from 'rxjs/operators';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {OperatorGroup} from '../../../shared/models/operator-group';
import {operatorGroupManagePageActions, operatorGroupManagePageOperatorGroups} from '../../store';

@Component({
  selector: 'jfundsvr-operator-group-manage-page',
  templateUrl: './operator-group-manage-page.component.html',
  styleUrls: ['./operator-group-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OperatorGroupManagePageComponent {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-currency-manage-page') b2 = true;
  readonly operatorGroupDataSource: DataSource<OperatorGroup>;
  operatorGroupColumns = ['id', 'name', 'btns'];

  constructor(private store: Store<any>,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.operatorGroupDataSource = new OperatorGroupDataSource(store);
  }

  delete(operatorGroup: OperatorGroup) {
    this.utilService.showConfirm()
      .pipe(
        switchMap(() => this.apiService.deleteOperatorGroup(operatorGroup.id)),
        finalize(() => console.log('test', 'finalize')),
      )
      .subscribe(() => {
        const action = new operatorGroupManagePageActions.DeleteSuccess(operatorGroup);
        this.store.dispatch(action);
        this.utilService.showSuccess();
      }, error => this.utilService.showError(error));
  }

}

class OperatorGroupDataSource extends DataSource<OperatorGroup> {
  data$: Observable<OperatorGroup[]>;

  constructor(private store: Store<any>) {
    super();
    this.data$ = this.store.select(operatorGroupManagePageOperatorGroups);
  }

  connect(collectionViewer: CollectionViewer): Observable<OperatorGroup[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
