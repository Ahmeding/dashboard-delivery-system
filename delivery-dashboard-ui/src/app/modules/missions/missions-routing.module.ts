import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AllMissionsComponent} from "./pages/all-missions/all-missions.component";
import {PlanningComponent} from "./pages/planning/planning.component";
import {ExpensesComponent} from "./pages/expenses/expenses.component";
import {NewExpenseComponent} from "./pages/expenses/new-expense/new-expense.component";
import {MissionDetailsComponent} from "./pages/all-missions/mission-details/mission-details.component";
import {AddMissionComponent} from "./pages/all-missions/add-mission/add-mission.component";
import {WeeklyPlanningComponent} from "./pages/planning/weekly-planning/weekly-planning.component";
import {authGuard} from "../../services/guard/auth.guard";

const routes: Routes = [
  { path: 'all', component: AllMissionsComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'add-mission/:dateDay', component: AddMissionComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'mission-details/:id', component: MissionDetailsComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },

  { path: 'planning', component: PlanningComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'w-planning', component: WeeklyPlanningComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_USER']} },
  { path: 'expenses', component: ExpensesComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'new-expense', component: NewExpenseComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MissionsRoutingModule { }
