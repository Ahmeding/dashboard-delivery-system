import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {authGuard} from "../../services/guard/auth.guard";
import {DashboardComponent} from "./pages/dashboard.component";

const routes: Routes = [
    {
      path: '',
      component: DashboardComponent,
      canActivate:[authGuard],
      data: {roles: ['ROLE_ADMIN']}
    }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }
