import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RepairesComponent} from "./pages/repaires/repaires.component";
import {VehiclesComponent} from "./pages/vehicles/vehicles.component";
import {NewRepaireComponent} from "./pages/repaires/new-repaire/new-repaire.component";
import {NewVehicleComponent} from "./pages/vehicles/new-vehicle/new-vehicle.component";

const routes: Routes = [

  { path: 'all', component: VehiclesComponent },
  { path: 'new-vehicle', component: NewVehicleComponent },
  { path: 'edit-vehicle/:id', component: NewVehicleComponent },
  { path: 'repairs', component: RepairesComponent },
  { path: 'new-repair', component: NewRepaireComponent },
  { path: 'edit-repair/:id', component: NewRepaireComponent },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VehiclesRoutingModule { }
