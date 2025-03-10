import { NgModule } from '@angular/core';
import {CommonModule, NgForOf, NgIf} from '@angular/common';

import { MissionsRoutingModule } from './missions-routing.module';
import { AllMissionsComponent } from './pages/all-missions/all-missions.component';
import { PlanningComponent } from './pages/planning/planning.component';
import { ExpensesComponent } from './pages/expenses/expenses.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterLink, RouterModule} from "@angular/router";
import {MissionDetailsComponent} from "./pages/all-missions/mission-details/mission-details.component";
import { NewExpenseComponent } from './pages/expenses/new-expense/new-expense.component';
import { AddMissionComponent } from './pages/all-missions/add-mission/add-mission.component';
import { ReviewMissionComponent } from './pages/all-missions/add-mission/review-mission/review-mission.component';
import { WeeklyPlanningComponent } from './pages/planning/weekly-planning/weekly-planning.component';
import { NewWeeklyPlanningModalComponent } from './pages/planning/new-weekly-planning-modal/new-weekly-planning-modal.component';
import { MissionCardComponent } from './pages/planning/weekly-planning/mission-card/mission-card.component';
import {PlanningService} from "./pages/planning/weekly-planning/services/planning-service";
import {BsDatepickerModule} from "ngx-bootstrap/datepicker";
import {NgSelectModule} from "@ng-select/ng-select";


@NgModule({
  declarations: [
    AllMissionsComponent,
    PlanningComponent,
    ExpensesComponent,
    MissionDetailsComponent,
    NewExpenseComponent,
    AddMissionComponent,
    ReviewMissionComponent,
    WeeklyPlanningComponent,
    NewWeeklyPlanningModalComponent,
    MissionCardComponent,
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
    RouterLink,
    RouterModule.forChild([
      {path: 'new', component: AddMissionComponent}
    ]),
    BsDatepickerModule,
    NgSelectModule
  ],
  providers: [PlanningService]
})
export class MissionsModule { }
