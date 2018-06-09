/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Operator} from '../../../shared/models/operator';


export const INIT_SUCCESS = '[OperatorUpdatePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { operator: Operator }) {
  }
}

export type Actions =
  | InitSuccess;
