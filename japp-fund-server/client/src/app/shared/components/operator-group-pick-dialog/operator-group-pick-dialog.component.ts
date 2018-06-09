import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Observable} from 'rxjs/Observable';
import {map} from 'rxjs/operators';
import {ApiService} from '../../../core/services/api.service';
import {DefaultCompare} from '../../../core/services/util.service';
import {OperatorGroup} from '../../models/operator-group';


@Component({
  selector: 'jfundsvr-operator-group-pick-dialog',
  templateUrl: './operator-group-pick-dialog.component.html',
  styleUrls: ['./operator-group-pick-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OperatorGroupPickDialogComponent {
  readonly form: FormGroup;
  operatorGroups$: Observable<OperatorGroup[]>;

  static open(dialog: MatDialog, data?: { operatorGroups?: OperatorGroup[] }): MatDialogRef<OperatorGroupPickDialogComponent> {
    data = data || {};
    data.operatorGroups = data.operatorGroups || [];
    return dialog.open(OperatorGroupPickDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  constructor(private fb: FormBuilder,
              private apiService: ApiService,
              public dialogRef: MatDialogRef<OperatorGroupPickDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { operatorGroups: OperatorGroup[] }) {
    const {operatorGroups} = data;
    this.form = this.fb.group({
      'operatorGroups': operatorGroups
    });
    this.operatorGroups$ = this.apiService.listOperatorGroup()
      .pipe(
        map(it => it.sort(DefaultCompare))
      );
  }

}
