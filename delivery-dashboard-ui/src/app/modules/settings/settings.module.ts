import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SettingsRoutingModule } from './settings-routing.module';
import { MyProfileComponent } from './pages/my-profile/my-profile.component';
import { UsersComponent } from './pages/users/users.component';
import { ServicesDeleiveryComponent } from './pages/services-deleivery/services-deleivery.component';


@NgModule({
  declarations: [
    MyProfileComponent,
    UsersComponent,
    ServicesDeleiveryComponent
  ],
  imports: [
    CommonModule,
    SettingsRoutingModule
  ]
})
export class SettingsModule { }
