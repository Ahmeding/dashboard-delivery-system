import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {VehiclesComponent} from "../vehicles/pages/vehicles/vehicles.component";
import {authGuard} from "../../services/guard/auth.guard";
import {StatisticsComponent} from "./pages/statistics/statistics.component";

const routes: Routes = [
  { path: 'statistics',
    component: StatisticsComponent,
    canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StatisticsRoutingModule { }
