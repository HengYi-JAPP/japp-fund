import {Injectable} from '@angular/core';
import {NativeDateAdapter} from '@angular/material';
import * as moment from 'moment';

@Injectable()
export class MyDateAdapter extends NativeDateAdapter {

  parse(value: any): Date | any {
    return value && moment(value).toDate();
  }

  format(date: Date, displayFormat: Object): string {
    return date && moment(date).format('YYYY-MM-DD');
  }

  getDateNames(): string[] {
    return ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31'];
  }
}
