import {Corporation} from './corporation';
import {Currency} from './currency';

export interface CorporationBalance {
  corporation: Corporation;
  currency: Currency;
  date: Date;
  balance: number;
}
