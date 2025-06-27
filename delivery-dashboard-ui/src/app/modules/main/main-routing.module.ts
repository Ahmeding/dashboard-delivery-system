import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './main.component';
import {authGuard} from "../../services/guard/auth.guard";
import {RoleRedirectGuard} from "../../services/guard/role-redirect.guard";

const routes: Routes = [
  { path: '', component: MainComponent,
    canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN','ROLE_USER'],
      children:[

      ]
  }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainRoutingModule { }
