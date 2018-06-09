import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PurposeManagePageComponent} from './containers/purpose-manage-page/purpose-manage-page.component';
import {PurposeUpdatePageComponent} from './containers/purpose-update-page/purpose-update-page.component';

const routes: Routes = [
  {
    path: 'manage',
    component: PurposeManagePageComponent,
  },
  {
    path: 'edit',
    component: PurposeUpdatePageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PurposeRoutingModule { }
