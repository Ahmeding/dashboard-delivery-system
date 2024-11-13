import { NgModule } from '@angular/core';
import {CommonModule, NgForOf, NgIf} from '@angular/common';

import { VehiclesRoutingModule } from './vehicles-routing.module';
import { RepairesComponent } from './pages/repaires/repaires.component';
import {VehiclesComponent} from "./pages/vehicles/vehicles.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NewRepaireComponent } from './pages/repaires/new-repaire/new-repaire.component';
import { NewVehicleComponent } from './pages/vehicles/new-vehicle/new-vehicle.component';
import { ViewVehiculeModalComponent } from './pages/vehicles/view-vehicule-modal/view-vehicule-modal.component';
import { ViewRepairModalComponent } from './pages/repaires/view-repair-modal/view-repair-modal.component';


@NgModule({
  declarations: [
    VehiclesComponent,
    RepairesComponent,
    NewRepaireComponent,
    NewVehicleComponent,
    ViewVehiculeModalComponent,
    ViewRepairModalComponent
  ],
    imports: [
        CommonModule,
        VehiclesRoutingModule,
        FormsModule,
        NgForOf,
        NgIf,
        ReactiveFormsModule
    ]
})
export class VehiclesModule { }
