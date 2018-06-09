import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CorporationManagePageComponent} from './containers/corporation-manage-page/corporation-manage-page.component';
import {CorporationUpdatePageComponent} from './containers/corporation-update-page/corporation-update-page.component';
import {CorporationImportPageComponent} from './containers/corporation-import-page/corporation-import-page.component';
import {CorporationBalancePageComponent} from './containers/corporation-balance-page/corporation-balance-page.component';

const routes: Routes = [
  {
    path: 'manage',
    component: CorporationManagePageComponent,
  },
  {
    path: 'edit',
    component: CorporationUpdatePageComponent,
  },
  {
    path: 'import',
    component: CorporationImportPageComponent,
  },
  {
    path: 'balance',
    component: CorporationBalancePageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CorporationRoutingModule { }
