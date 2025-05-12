import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DriverPlanningRoutingModule } from './driver-planning-routing.module';
import { WeeklyPlanningComponent } from './pages/weekly-planning/weekly-planning.component';
import {MissionsModule} from "../missions/missions.module";
import {MissionCardComponent} from "../missions/pages/planning/weekly-planning/mission-card/mission-card.component";
import { ManageTripMissionModalComponent } from './pages/components/manage-trip-mission-modal/manage-trip-mission-modal.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { ManageTripMissionComponent } from './pages/manage-trip-mission/manage-trip-mission.component';


@NgModule({
  declarations: [
    WeeklyPlanningComponent,
    ManageTripMissionModalComponent,
    ManageTripMissionComponent,
  ],
  imports: [
    CommonModule,
    DriverPlanningRoutingModule,
    MissionsModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class DriverPlanningModule { }
