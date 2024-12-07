import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AllMissionsComponent} from "./pages/all-missions/all-missions.component";
import {PlanningComponent} from "./pages/planning/planning.component";
import {ExpensesComponent} from "./pages/expenses/expenses.component";
import {NewExpenseComponent} from "./pages/expenses/new-expense/new-expense.component";
import {NewMissionComponent} from "./pages/all-missions/new-mission/new-mission.component";
import {MissionDetailsComponent} from "./pages/all-missions/mission-details/mission-details.component";
import {AddMissionComponent} from "./pages/all-missions/add-mission/add-mission.component";
import {WeeklyPlanningComponent} from "./pages/planning/weekly-planning/weekly-planning.component";

const routes: Routes = [
  { path: 'all', component: AllMissionsComponent },
  { path: 'new-mission', component: NewMissionComponent },
  { path: 'add-mission', component: AddMissionComponent },
  { path: 'mission-details/:id', component: MissionDetailsComponent },

  { path: 'planning', component: PlanningComponent },
  { path: 'w-planning', component: WeeklyPlanningComponent },
  { path: 'expenses', component: ExpensesComponent },
  { path: 'new-expense', component: NewExpenseComponent },
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MissionsRoutingModule { }
