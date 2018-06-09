import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {SharedModule} from '../shared/shared.module';
import {CorporationBalancePageComponent} from './containers/corporation-balance-page/corporation-balance-page.component';
import {CorporationFundPageComponent} from './containers/corporation-fund-page/corporation-fund-page.component';
import {CorporationImportPageComponent} from './containers/corporation-import-page/corporation-import-page.component';
import {CorporationManagePageComponent} from './containers/corporation-manage-page/corporation-manage-page.component';
import {CorporationUpdatePageComponent} from './containers/corporation-update-page/corporation-update-page.component';
import {CorporationRoutingModule} from './corporation-routing.module';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [];
export const COMPONENTS = [
  CorporationManagePageComponent,
  CorporationUpdatePageComponent,
  CorporationImportPageComponent,
  CorporationBalancePageComponent,
  CorporationFundPageComponent,
  ...ENTRYCOMPONENTS
];
@NgModule({
  imports: [
    SharedModule,
    CorporationRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
})
export class CorporationModule { }
