import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SettingsRoutingModule } from './settings-routing.module';
import { MyProfileComponent } from './pages/my-profile/my-profile.component';
import { UsersComponent } from './pages/users/users.component';
import { ServicesDeleiveryComponent } from './pages/services-deleivery/services-deleivery.component';
import { NewServiceDelivComponent } from './pages/services-deleivery/new-service-deliv/new-service-deliv.component';
import { NewUserComponent } from './pages/users/new-user/new-user.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    MyProfileComponent,
    UsersComponent,
    ServicesDeleiveryComponent,
    NewServiceDelivComponent,
    NewUserComponent
  ],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class SettingsModule { }
