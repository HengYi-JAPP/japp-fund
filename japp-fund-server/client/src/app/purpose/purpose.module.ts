import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {CorporationModule} from '../corporation/corporation.module';
import {SharedModule} from '../shared/shared.module';
import {PurposeManagePageComponent} from './containers/purpose-manage-page/purpose-manage-page.component';
import {PurposeUpdatePageComponent} from './containers/purpose-update-page/purpose-update-page.component';
import {PurposeRoutingModule} from './purpose-routing.module';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [];
export const COMPONENTS = [
  PurposeManagePageComponent,
  PurposeUpdatePageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    SharedModule,
    PurposeRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
})
export class PurposeModule {
}
