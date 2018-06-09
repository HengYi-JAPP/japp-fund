import {NgModule, Optional, SkipSelf} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {SharedModule} from '../shared/shared.module';
import {ConfirmDialogComponent} from './components/confirm-dialog/confirm-dialog.component';
import {AppComponent} from './containers/app/app.component';
import {NotFoundPageComponent} from './containers/not-found-page/not-found-page.component';
import {CoreRoutingModule} from './core-routing.module';
import {AdminGuard} from './services/admin.guard';
import {ApiService} from './services/api.service';
import {UtilService} from './services/util.service';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [
  ConfirmDialogComponent,
];
export const COMPONENTS = [
  AppComponent,
  NotFoundPageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    SharedModule,
    CoreRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    UtilService,
    ApiService,
    AdminGuard,
  ]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error(
        'CoreModule is already loaded. Import it in the AppModule only');
    }
  }
}
