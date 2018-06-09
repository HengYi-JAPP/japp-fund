import {Injectable} from '@angular/core';
import {MatPaginatorIntl} from '@angular/material';

@Injectable()
export class MyPaginatorIntl extends MatPaginatorIntl {
  itemsPerPageLabel = '每页数量';
  nextPageLabel = '下一页';
  previousPageLabel = '上一页';
}
