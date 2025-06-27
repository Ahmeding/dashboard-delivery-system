import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StatisticsRoutingModule } from './statistics-routing.module';
import {StatisticsComponent} from "./pages/statistics/statistics.component";
import {DashboardModule} from "../dashboard/dashboard.module";
import {StatisticsCardComponent} from "../../components/dashboard/statistics-card/statistics-card.component";
import {ChartComponent} from "../../components/dashboard/chart/chart.component";


@NgModule({
  declarations: [
    StatisticsComponent,
    StatisticsCardComponent,
    ChartComponent,
  ],

  exports: [
  StatisticsCardComponent,
  ChartComponent
],
  imports: [
    CommonModule,
    StatisticsRoutingModule,
  ]
})
export class StatisticsModule { }
