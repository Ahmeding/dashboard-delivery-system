import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MyProfileComponent} from "./pages/my-profile/my-profile.component";
import {UsersComponent} from "./pages/users/users.component";
import {ServicesDeleiveryComponent} from "./pages/services-deleivery/services-deleivery.component";
import {NewServiceDelivComponent} from "./pages/services-deleivery/new-service-deliv/new-service-deliv.component";
import {NewUserComponent} from "./pages/users/new-user/new-user.component";
import {DeliveryPointCategoryComponent} from "./pages/delivery-point-category/delivery-point-category.component";
import {authGuard} from "../../services/guard/auth.guard";

const routes: Routes = [
  { path: 'profile', component: MyProfileComponent,
        canActivate:[authGuard] },
  { path: 'users', component: UsersComponent,
        canActivate:[authGuard] },
  { path: 'new-user', component: NewUserComponent,
        canActivate:[authGuard] },
  { path: 'services', component: ServicesDeleiveryComponent,
        canActivate:[authGuard] },
  { path: 'categories', component: DeliveryPointCategoryComponent,
        canActivate:[authGuard] },
  { path: 'new-service-deliv', component: NewServiceDelivComponent,
        canActivate:[authGuard]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettingsRoutingModule { }
