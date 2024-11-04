import { NgModule } from '@angular/core';
import {CommonModule, NgForOf, NgIf} from '@angular/common';

import { MissionsRoutingModule } from './missions-routing.module';
import { AllMissionsComponent } from './pages/all-missions/all-missions.component';
import { PlanningComponent } from './pages/planning/planning.component';
import { ExpensesComponent } from './pages/expenses/expenses.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterLink} from "@angular/router";
import {MissionDetailsComponent} from "./pages/all-missions/mission-details/mission-details.component";
import {NewMissionComponent} from "./pages/all-missions/new-mission/new-mission.component";
import { NewExpenseComponent } from './pages/expenses/new-expense/new-expense.component';


@NgModule({
  declarations: [
    AllMissionsComponent,
    PlanningComponent,
    ExpensesComponent,
    MissionDetailsComponent,
    NewMissionComponent,
    NewExpenseComponent,
  ],
  imports: [
    CommonModule,
    MissionsRoutingModule,
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    RouterLink,
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    RouterLink
  ]
})
export class MissionsModule { }
