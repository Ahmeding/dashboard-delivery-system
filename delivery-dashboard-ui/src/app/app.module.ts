import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {FullCalendarModule} from "@fullcalendar/angular";
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";
import {NgbDropdownModule, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {NgChartsModule} from "ng2-charts";
import {ReactiveFormsModule} from "@angular/forms";
import {ThemeService} from "./services/theme/theme.service";
import {LoginComponent} from "./pages/login/login.component";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {HttpTokenInterceptor} from "./services/interceptor/http-token.interceptor";
import {ToastrModule} from "ngx-toastr";
import {
  ConfirmBoxConfigModule,
  ConfirmBoxEvokeService,
  NgxAwesomePopupModule
} from "@costlydeveloper/ngx-awesome-popup";
import {BsDatepickerModule} from "ngx-bootstrap/datepicker";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
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
    HttpClientModule,
    NgxAwesomePopupModule.forRoot(), // Essential, mandatory main module.
    ConfirmBoxConfigModule.forRoot(),
    ToastrModule.forRoot({
      progressBar:true,
      closeButton:true,
      newestOnTop:true,
      tapToDismiss:true,
      positionClass:'toast-top-right',
      timeOut:8000
    }),
    BrowserAnimationsModule
  ],
  providers: [
    ThemeService,
    HttpClient,
    {
      provide:HTTP_INTERCEPTORS,
      useClass:HttpTokenInterceptor,
      multi:true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
