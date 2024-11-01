import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VehiclesRoutingModule } from './vehicles-routing.module';
import { RepairesComponent } from './pages/repaires/repaires.component';
import {VehiclesComponent} from "./pages/vehicles/vehicles.component";


@NgModule({
  declarations: [
    VehiclesComponent,
    RepairesComponent
  ],
  imports: [
    CommonModule,
    VehiclesRoutingModule
  ]
})
export class VehiclesModule { }
