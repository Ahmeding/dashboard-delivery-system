import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DriversRoutingModule } from './drivers-routing.module';
import { ContractsComponent } from './pages/contracts/contracts.component';
import {DriversComponent} from "./pages/drivers/drivers.component";


@NgModule({
  declarations: [
    DriversComponent,
    ContractsComponent
  ],
  imports: [
    CommonModule,
    DriversRoutingModule
  ]
})
export class DriversModule { }
