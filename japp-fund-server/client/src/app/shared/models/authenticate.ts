import {Operator} from './operator';

export interface Authenticate {
  loginId: string;
  loginPassword: string;
}

export interface AuthenticateResponse {
  admin: boolean;
  operator: Operator;
}
