import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AdminGuard} from './services/admin.guard';

const routes: Routes = [
  {path: '', redirectTo: '/operators/manage', pathMatch: 'full'},
  {
    path: 'corporations',
    loadChildren: '../corporation/corporation.module#CorporationModule',
    canActivate: [AdminGuard],
  },
  {
    path: 'currencies',
    loadChildren: '../currency/currency.module#CurrencyModule',
    canActivate: [AdminGuard]
  },
  {
    path: 'purposes',
    loadChildren: '../purpose/purpose.module#PurposeModule',
    canActivate: [AdminGuard]
  },
  {
    path: 'operators',
    loadChildren: '../operator/operator.module#OperatorModule',
    canActivate: [AdminGuard]
  },
  {
    path: 'operatorGroups',
    loadChildren: '../operator-group/operator-group.module#OperatorGroupModule',
    canActivate: [AdminGuard],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class CoreRoutingModule {
}
