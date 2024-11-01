import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MissionsRoutingModule } from './missions-routing.module';
import { AllMissionsComponent } from './pages/all-missions/all-missions.component';
import { PlanningComponent } from './pages/planning/planning.component';
import { ExpensesComponent } from './pages/expenses/expenses.component';


@NgModule({
  declarations: [
    AllMissionsComponent,
    PlanningComponent,
    ExpensesComponent
  ],
  imports: [
    CommonModule,
    MissionsRoutingModule
  ]
})
export class MissionsModule { }
