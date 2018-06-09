import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, forwardRef, OnDestroy} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {MatDialog} from '@angular/material';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import {OperatorGroup} from '../../models/operator-group';
import {OperatorGroupPickDialogComponent} from '../operator-group-pick-dialog/operator-group-pick-dialog.component';

@Component({
  selector: 'jfundsvr-operator-group-input',
  templateUrl: './operator-group-input.component.html',
  styleUrls: ['./operator-group-input.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => OperatorGroupInputComponent),
    multi: true
  }],
})
export class OperatorGroupInputComponent implements ControlValueAccessor, OnDestroy {
  private subscriptions = [];
  readonly operatorGroupDataSource = new OperatorGroupDataSource(new BehaviorSubject<OperatorGroup[]>([]));
  operatorGroupColumns = ['name', 'btns'];

  constructor(private dialog: MatDialog) {
  }

  update() {
    OperatorGroupPickDialogComponent.open(this.dialog, {operatorGroups: this.operatorGroupDataSource.copy});
  }

  delete(operatorGroup: OperatorGroup) {
    let operatorGroups = this.operatorGroupDataSource.copy;
    operatorGroups = operatorGroups.filter(it => it.id !== operatorGroup.id);
    this.handleChange(operatorGroups);
  }

  private handleChange(operatorGroups: OperatorGroup[]) {
    this.operatorGroupDataSource.next(operatorGroups);
    this.onModelChange(operatorGroups);
  }

  writeValue(operatorGroups: OperatorGroup[]): void {
    this.operatorGroupDataSource.next(operatorGroups);
  }

  onModelChange: Function = () => {
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  onModelTouched: Function = () => {
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }
}

class OperatorGroupDataSource extends DataSource<OperatorGroup> {
  constructor(private data$: BehaviorSubject<OperatorGroup[]>) {
    super();
  }

  connect(collectionViewer: CollectionViewer): Observable<OperatorGroup[]> {
    return this.data$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

  next(operatorGroups: OperatorGroup[]) {
    this.data$.next(operatorGroups || []);
  }

  get copy(): OperatorGroup[] {
    const operatorGroups = this.data$.getValue() || [];
    return [...operatorGroups];
  }

}
