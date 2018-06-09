import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {OperatorGroupModule} from '../operator-group/operator-group.module';
import {SharedModule} from '../shared/shared.module';
import {OperatorImportPageComponent} from './containers/operator-import-page/operator-import-page.component';
import {OperatorManagePageComponent} from './containers/operator-manage-page/operator-manage-page.component';
import {OperatorUpdatePageComponent} from './containers/operator-update-page/operator-update-page.component';
import {OperatorRoutingModule} from './operator-routing.module';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [];
export const COMPONENTS = [
  OperatorManagePageComponent,
  OperatorImportPageComponent,
  OperatorUpdatePageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    SharedModule,
    OperatorRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
})
export class OperatorModule {
}
