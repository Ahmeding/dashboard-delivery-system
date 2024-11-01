import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SidebarComponent } from './components/shared/sidebar/sidebar.component';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import {FullCalendarModule} from "@fullcalendar/angular";
import { NotificationsComponent } from './components/shared/notifications/notifications.component';
import { ProfileMenuComponent } from './components/shared/profile-menu/profile-menu.component';
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";
import {NgbDropdownModule, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {NgChartsModule} from "ng2-charts";
import {ReactiveFormsModule} from "@angular/forms";
import {ThemeService} from "./services/theme/theme.service";
import {LoginComponent} from "./pages/login/login.component";
import {FooterComponent} from "./components/shared/footer/footer.component";
import {ThemeToggleComponent} from "./components/shared/theme-toggle/theme-toggle.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {StatisticsCardComponent} from "./components/dashboard/statistics-card/statistics-card.component";
import {ChartComponent} from "./components/dashboard/chart/chart.component";
import {PlanningComponent} from "./components/dashboard/planning/planning.component";
import {EventModalComponent} from "./components/dashboard/planning/event-modal/event-modal/event-modal.component";

@NgModule({
  declarations: [
    AppComponent,
   /* DashboardComponent,*/
   /* NavbarComponent,
    SidebarComponent,
    NotificationsComponent,
    ProfileMenuComponent,
/!*    StatisticsCardComponent,
    ChartComponent,
    PlanningComponent,
    EventModalComponent,*!/
    FooterComponent,
    ThemeToggleComponent,*/
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    RouterModule,
    NgbModule,
    FullCalendarModule,
    ReactiveFormsModule,
    NgChartsModule,
    NgbDropdownModule,
  ],
  providers: [ThemeService],
  bootstrap: [AppComponent]
})
export class AppModule { }
