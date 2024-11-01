import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MyProfileComponent} from "./pages/my-profile/my-profile.component";
import {UsersComponent} from "./pages/users/users.component";
import {ServicesDeleiveryComponent} from "./pages/services-deleivery/services-deleivery.component";

const routes: Routes = [
  { path: 'profile', component: MyProfileComponent },
  { path: 'users', component: UsersComponent },
  { path: 'services', component: ServicesDeleiveryComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettingsRoutingModule { }
