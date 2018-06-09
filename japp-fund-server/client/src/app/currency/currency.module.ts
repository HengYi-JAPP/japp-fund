import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {SharedModule} from '../shared/shared.module';
import {CurrencyImportPageComponent} from './containers/currency-import-page/currency-import-page.component';
import {CurrencyManagePageComponent} from './containers/currency-manage-page/currency-manage-page.component';
import {CurrencyUpdatePageComponent} from './containers/currency-update-page/currency-update-page.component';
import {CurrencyRoutingModule} from './currency-routing.module';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [];
export const COMPONENTS = [
  CurrencyManagePageComponent,
  CurrencyUpdatePageComponent,
  CurrencyImportPageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    SharedModule,
    CurrencyRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
})
export class CurrencyModule {
}
