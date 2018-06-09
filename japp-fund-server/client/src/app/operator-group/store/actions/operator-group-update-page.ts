/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {OperatorGroup} from '../../../shared/models/operator-group';

export const INIT_SUCCESS = '[OperatorGroupUpdatePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { operatorGroup: OperatorGroup }) {
  }
}

export type Actions =
  | InitSuccess;
