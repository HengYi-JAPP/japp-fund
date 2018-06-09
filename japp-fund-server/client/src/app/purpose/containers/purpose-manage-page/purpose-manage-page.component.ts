import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, HostBinding} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {finalize, switchMap} from 'rxjs/operators';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {Purpose} from '../../../shared/models/purpose';
import {purposeManagePageActions, purposeManagePagePurposes} from '../../store';

@Component({
  selector: 'jfundsvr-purpose-manage-page',
  templateUrl: './purpose-manage-page.component.html',
  styleUrls: ['./purpose-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PurposeManagePageComponent {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-purpose-manage-page') b2 = true;
  readonly purposeDataSource: DataSource<Purpose>;
  purposeColumns = ['id', 'name', 'sortBy', 'btns'];

  constructor(private store: Store<any>,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.purposeDataSource = new PurposeDataSource(store);

    this.store.select(purposeManagePagePurposes).pipe()
      .subscribe(it => console.log('test', it))
  }

  delete(purpose: Purpose) {
    console.log('test', 'loading');
    this.utilService.showConfirm()
      .pipe(
        switchMap(() => this.apiService.deletePurpose(purpose.id)),
        finalize(() => console.log('test', 'finalize')),
      )
      .subscribe(() => {
        const action = new purposeManagePageActions.DeleteSuccess({id: purpose.id});
        this.store.dispatch(action);
        this.utilService.showSuccess();
      }, error => this.utilService.showError(error));
  }
}

class PurposeDataSource extends DataSource<Purpose> {
  purposes$: Observable<Purpose[]>;

  constructor(private store: Store<any>) {
    super();
    this.purposes$ = this.store.select(purposeManagePagePurposes);
  }

  connect(collectionViewer: CollectionViewer): Observable<Purpose[]> {
    return this.purposes$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }
}
