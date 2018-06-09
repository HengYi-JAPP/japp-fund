import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {baseApiUrl, headers} from '../../../environments/environment';
import {AuthenticateResponse} from '../../shared/models/authenticate';
import {Corporation} from '../../shared/models/corporation';
import {CorporationBalance} from '../../shared/models/corporation-balance';
import {CorporationSuggest} from '../../shared/models/corporation-suggest';
import {Currency} from '../../shared/models/currency';
import {CurrencySuggest} from '../../shared/models/currency-suggest';
import {Operator} from '../../shared/models/operator';
import {OperatorGroup} from '../../shared/models/operator-group';
import {OperatorPermission} from '../../shared/models/operator-permission';
import {OperatorSuggest} from '../../shared/models/operator-suggest';
import {Purpose} from '../../shared/models/purpose';

@Injectable()
export class ApiService {
  constructor(private http: HttpClient) {
  }

  getAuth(): Observable<AuthenticateResponse> {
    return this.http.get<AuthenticateResponse>(`${baseApiUrl}/auth`, {headers});
  }

  createCorporation(corporationSuggest: CorporationSuggest): Observable<Corporation> {
    return this.http.post<Corporation>(`${baseApiUrl}/admin/corporations`, corporationSuggest, {headers});
  }

  updateCorporation(id: string, corporation: Corporation): Observable<Corporation> {
    return this.http.put<Corporation>(`${baseApiUrl}/admin/corporations/${id}`, corporation, {headers});
  }

  getCorporation(id: string): Observable<Corporation> {
    return this.http.get<Corporation>(`${baseApiUrl}/admin/corporations/${id}`, {headers});
  }

  listCorporation(): Observable<Corporation[]> {
    return this.http.get<Corporation[]>(`${baseApiUrl}/admin/corporations`, {headers});
  }

  listCorporationSuggest(q: string): Observable<CorporationSuggest[]> {
    return q ? this.http.get<CorporationSuggest[]>(`${baseApiUrl}/admin/suggestCorporations?q=${q}`, {headers}) : of([]);
  }

  listCorporationBalance(params: HttpParams): Observable<CorporationBalance[]> {
    return this.http.get<CorporationBalance[]>(`${baseApiUrl}/balances`, {params, headers});
  }

  createCurrency(currencySuggest: CurrencySuggest): Observable<Currency> {
    return this.http.post<Currency>(`${baseApiUrl}/admin/currencies`, currencySuggest, {headers});
  }

  updateCurrency(id: string, currency: Currency): Observable<Currency> {
    return this.http.put<Currency>(`${baseApiUrl}/admin/currencies/${id}`, currency, {headers});
  }

  getCurrency(id: string): Observable<Currency> {
    return this.http.get<Currency>(`${baseApiUrl}/admin/currencies/${id}`, {headers});
  }

  deleteCurrency(id: string): Observable<any> {
    return this.http.delete(`${baseApiUrl}/admin/currencies/${id}`, {headers});
  }

  listCurrency(): Observable<Currency[]> {
    return this.http.get<Currency[]>(`${baseApiUrl}/admin/currencies`, {headers});
  }

  savePurpose(purpose: Purpose): Observable<Purpose> {
    return purpose.id ? this.updatePurpose(purpose.id, purpose) : this.createPurpose(purpose);
  }

  createPurpose(purpose: Purpose): Observable<Purpose> {
    return this.http.post<Purpose>(`${baseApiUrl}/admin/purposes`, purpose, {headers});
  }

  updatePurpose(id: string, purpose: Purpose): Observable<Purpose> {
    return this.http.put<Purpose>(`${baseApiUrl}/admin/purposes/${id}`, purpose, {headers});
  }

  getPurpose(id: string): Observable<Purpose> {
    return this.http.get<Purpose>(`${baseApiUrl}/admin/purposes/${id}`, {headers});
  }

  deletePurpose(id: string): Observable<any> {
    return this.http.delete(`${baseApiUrl}/admin/purposes/${id}`, {headers});
  }

  listPurpose(params?: HttpParams): Observable<Purpose[]> {
    return this.http.get<Purpose[]>(`${baseApiUrl}/admin/purposes`, {params, headers});
  }

  saveOperatorGroup(operatorGroup: OperatorGroup): Observable<OperatorGroup> {
    return operatorGroup.id ? this.updateOperatorGroup(operatorGroup.id, operatorGroup) : this.createOperatorGroup(operatorGroup);
  }

  createOperatorGroup(operatorGroup: OperatorGroup): Observable<OperatorGroup> {
    return this.http.post<OperatorGroup>(`${baseApiUrl}/admin/operatorGroups`, operatorGroup, {headers});
  }

  updateOperatorGroup(id: string, operatorGroup: OperatorGroup): Observable<OperatorGroup> {
    return this.http.put<OperatorGroup>(`${baseApiUrl}/admin/operatorGroups/${id}`, operatorGroup, {headers});
  }

  getOperatorGroup(id: string): Observable<OperatorGroup> {
    return this.http.get<OperatorGroup>(`${baseApiUrl}/admin/operatorGroups/${id}`, {headers});
  }

  deleteOperatorGroup(id: string): Observable<any> {
    return this.http.delete(`${baseApiUrl}/admin/operatorGroups/${id}`, {headers});
  }

  listOperatorGroup(): Observable<OperatorGroup[]> {
    return this.http.get<OperatorGroup[]>(`${baseApiUrl}/admin/operatorGroups`, {headers});
  }

  createOperator(operatorSuggest: OperatorSuggest): Observable<Operator> {
    return this.http.post<Operator>(`${baseApiUrl}/admin/operators`, operatorSuggest, {headers});
  }

  updateOperator(id: string, operator: Operator): Observable<Operator> {
    return this.http.put<Operator>(`${baseApiUrl}/admin/operators/${id}`, operator, {headers});
  }

  getOperator(id: string): Observable<Operator> {
    return this.http.get<Operator>(`${baseApiUrl}/admin/operators/${id}`, {headers});
  }

  getOperator_Permission(id: string): Observable<OperatorPermission> {
    return this.http.get<OperatorPermission>(`${baseApiUrl}/admin/operators/${id}/permission`, {headers});
  }

  listOperator(): Observable<Operator[]> {
    return this.http.get<Operator[]>(`${baseApiUrl}/admin/operators`, {headers});
  }

  listOperatorSuggest(q: string): Observable<OperatorSuggest[]> {
    return q ? this.http.get<OperatorSuggest[]>(`${baseApiUrl}/admin/suggestOperators?q=${q}`, {headers}) : of([]);
  }

  getPermission(id: string): Observable<OperatorPermission> {
    return this.http.get<OperatorPermission>(`${baseApiUrl}/admin/operators/${id}/permission`, {headers});
  }

  updatePermission(id: string, permission: OperatorPermission): Observable<OperatorPermission> {
    return this.http.put<OperatorPermission>(`${baseApiUrl}/admin/operators/${id}/permission`, permission, {headers});
  }
}
