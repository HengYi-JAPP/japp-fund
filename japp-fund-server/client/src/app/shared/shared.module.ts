import {CdkTableModule} from '@angular/cdk/table';
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {
  DateAdapter, MAT_DATE_LOCALE, MatButtonModule, MatCardModule, MatCheckboxModule, MatDatepickerModule,
  MatDialogModule, MatIconModule, MatInputModule, MatListModule, MatMenuModule, MatNativeDateModule, MatPaginatorIntl,
  MatPaginatorModule, MatProgressBarModule, MatProgressSpinnerModule, MatSelectModule, MatSidenavModule,
  MatSnackBarModule, MatTableModule, MatTabsModule, MatToolbarModule
} from '@angular/material';
import {RouterModule} from '@angular/router';
import {TranslateModule} from '@ngx-translate/core';
import {OperatorGroupInputComponent} from './components/operator-group-input/operator-group-input.component';
import {OperatorGroupPickDialogComponent} from './components/operator-group-pick-dialog/operator-group-pick-dialog.component';
import {PermissionInputComponent} from './components/permission-input/permission-input.component';
import {PermissionUpdateDialogComponent} from './components/permission-update-dialog/permission-update-dialog.component';
import {MyDateAdapter} from './services/my-date-adapter';
import {MyPaginatorIntl} from './services/my-paginator-intl';
import {OperatorAvatarPipe} from './services/operator-avatar.pipe';
import {PermissionCorporationNamePipe} from './services/permission-corporation-name.pipe';
import {PermissionCurrencyNamePipe} from './services/permission-currency-name.pipe';
import {PermissionRoleTypeNamePipe} from './services/permission-role-type-name.pipe';

export const ENTRYCOMPONENTS = [
  PermissionUpdateDialogComponent,
  OperatorGroupPickDialogComponent,
];
export const COMPONENTS = [
  OperatorAvatarPipe,
  PermissionCorporationNamePipe,
  PermissionCurrencyNamePipe,
  PermissionRoleTypeNamePipe,
  PermissionInputComponent,
  OperatorGroupInputComponent,
  ...ENTRYCOMPONENTS
];
export const IMPORTS = [
  RouterModule,
  CommonModule,
  FormsModule,
  ReactiveFormsModule,
  TranslateModule,
  FlexLayoutModule,
  CdkTableModule,
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule,
  MatDatepickerModule,
  MatDialogModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatSelectModule,
  MatSidenavModule,
  MatSnackBarModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
];

@NgModule({
  declarations: COMPONENTS,
  entryComponents: ENTRYCOMPONENTS,
  imports: IMPORTS,
  exports: [IMPORTS, ...COMPONENTS],
  providers: [
    {provide: DateAdapter, useClass: MyDateAdapter},
    {provide: MAT_DATE_LOCALE, useValue: 'zh-CN'},
    {provide: MatPaginatorIntl, useClass: MyPaginatorIntl},
  ]
})
export class SharedModule {
}
