import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {CurrencyManagePageComponent} from './containers/currency-manage-page/currency-manage-page.component';
import {CurrencyUpdatePageComponent} from './containers/currency-update-page/currency-update-page.component';
import {CurrencyImportPageComponent} from './containers/currency-import-page/currency-import-page.component';

const routes: Routes = [
  {
    path: 'manage',
    component: CurrencyManagePageComponent,
  },
  {
    path: 'edit',
    component: CurrencyUpdatePageComponent,
  },
  {
    path: 'import',
    component: CurrencyImportPageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CurrencyRoutingModule { }
