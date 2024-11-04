import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MyProfileComponent} from "./pages/my-profile/my-profile.component";
import {UsersComponent} from "./pages/users/users.component";
import {ServicesDeleiveryComponent} from "./pages/services-deleivery/services-deleivery.component";
import {NewServiceDelivComponent} from "./pages/services-deleivery/new-service-deliv/new-service-deliv.component";
import {NewUserComponent} from "./pages/users/new-user/new-user.component";

const routes: Routes = [
  { path: 'profile', component: MyProfileComponent },
  { path: 'users', component: UsersComponent },
  { path: 'new-user', component: NewUserComponent },
  { path: 'services', component: ServicesDeleiveryComponent },
  { path: 'new-service-deliv', component: NewServiceDelivComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettingsRoutingModule { }
