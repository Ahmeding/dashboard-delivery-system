import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AllMissionsComponent} from "./pages/all-missions/all-missions.component";
import {PlanningComponent} from "./pages/planning/planning.component";
import {ExpensesComponent} from "./pages/expenses/expenses.component";

const routes: Routes = [
  { path: 'all', component: AllMissionsComponent },
  { path: 'planning', component: PlanningComponent },
  { path: 'expenses', component: ExpensesComponent }
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MissionsRoutingModule { }
