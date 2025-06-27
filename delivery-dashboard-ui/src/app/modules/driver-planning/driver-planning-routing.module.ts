import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {authGuard} from "../../services/guard/auth.guard";
import {StatisticsComponent} from "../statistics/pages/statistics/statistics.component";
import {WeeklyPlanningComponent} from "./pages/weekly-planning/weekly-planning.component";
import {ManageTripMissionComponent} from "./pages/manage-trip-mission/manage-trip-mission.component";

const routes: Routes = [
  {
    path:'my-weekly-p',
    component: WeeklyPlanningComponent,
    canActivate:[authGuard],
    data: {roles: ['ROLE_USER']}
  },
  {
    path:'manage-trip-mission/:id',
    component: ManageTripMissionComponent,
    canActivate:[authGuard],
    data: {roles: ['ROLE_USER']}
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverPlanningRoutingModule { }
