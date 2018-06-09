import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {OperatorGroupManagePageComponent} from './containers/operator-group-manage-page/operator-group-manage-page.component';
import {OperatorGroupUpdatePageComponent} from './containers/operator-group-update-page/operator-group-update-page.component';

const routes: Routes = [
  {
    path: 'manage',
    component: OperatorGroupManagePageComponent,
  },
  {
    path: 'edit',
    component: OperatorGroupUpdatePageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OperatorGroupRoutingModule { }
