import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DriverPlanningRoutingModule } from './driver-planning-routing.module';
import { WeeklyPlanningComponent } from './pages/weekly-planning/weekly-planning.component';
import {MissionsModule} from "../missions/missions.module";
import {MissionCardComponent} from "../missions/pages/planning/weekly-planning/mission-card/mission-card.component";


@NgModule({
  declarations: [
    WeeklyPlanningComponent,
  ],
  imports: [
    CommonModule,
    DriverPlanningRoutingModule,
    MissionsModule
  ]
})
export class DriverPlanningModule { }
