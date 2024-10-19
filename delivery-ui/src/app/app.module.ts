import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {HttpTokenInterceptor} from "./services/interceptor/http-token.interceptor";
import {ToastrModule} from "ngx-toastr";
import {NgxPaginationModule} from "ngx-pagination";
import {ConfirmBoxConfigModule, NgxAwesomePopupModule} from "@costlydeveloper/ngx-awesome-popup";
import {NgChartsModule} from "ng2-charts";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
      ReactiveFormsModule,
      HttpClientModule,
      NgxPaginationModule,
      NgChartsModule,
      NgxAwesomePopupModule.forRoot(), // Essential, mandatory main module.
      ConfirmBoxConfigModule.forRoot(),

      ToastrModule.forRoot({
        progressBar:true,
        closeButton:true,
        newestOnTop:true,
        tapToDismiss:true,
        positionClass:'toast-top-right',
        timeOut:8000
      })
    ],
  providers: [
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
