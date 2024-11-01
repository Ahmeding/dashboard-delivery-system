import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import {DashboardComponent} from "../../components/dashboard/dashboard.component";
import {StatisticsCardComponent} from "../../components/dashboard/statistics-card/statistics-card.component";
import {ChartComponent} from "../../components/dashboard/chart/chart.component";
import {PlanningComponent} from "../../components/dashboard/planning/planning.component";
import {EventModalComponent} from "../../components/dashboard/planning/event-modal/event-modal/event-modal.component";
import {ReactiveFormsModule} from "@angular/forms";
import {FullCalendarModule} from "@fullcalendar/angular";
import {BrowserModule} from "@angular/platform-browser";
import {AppRoutingModule} from "../../app-routing.module";
import {RouterModule} from "@angular/router";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {NgChartsModule} from "ng2-charts";


@NgModule({
  declarations: [
    DashboardComponent,
    StatisticsCardComponent,
    ChartComponent,
    PlanningComponent,
    EventModalComponent,
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    ReactiveFormsModule,
    FullCalendarModule,
    NgbModule,
    NgChartsModule,
  ]
})
export class DashboardModule { }
