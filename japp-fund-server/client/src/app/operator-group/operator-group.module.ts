import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {SharedModule} from '../shared/shared.module';
import {OperatorGroupManagePageComponent} from './containers/operator-group-manage-page/operator-group-manage-page.component';
import {OperatorGroupUpdatePageComponent} from './containers/operator-group-update-page/operator-group-update-page.component';
import {OperatorGroupRoutingModule} from './operator-group-routing.module';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [
];
export const COMPONENTS = [
  OperatorGroupManagePageComponent,
  OperatorGroupUpdatePageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    SharedModule,
    OperatorGroupRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
})
export class OperatorGroupModule {
}
