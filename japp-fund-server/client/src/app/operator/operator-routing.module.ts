import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {OperatorManagePageComponent} from './containers/operator-manage-page/operator-manage-page.component';
import {OperatorImportPageComponent} from './containers/operator-import-page/operator-import-page.component';
import {OperatorUpdatePageComponent} from './containers/operator-update-page/operator-update-page.component';

const routes: Routes = [
  {
    path: 'manage',
    component: OperatorManagePageComponent,
  },
  {
    path: 'edit',
    component: OperatorUpdatePageComponent,
  },
  {
    path: 'import',
    component: OperatorImportPageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OperatorRoutingModule { }
