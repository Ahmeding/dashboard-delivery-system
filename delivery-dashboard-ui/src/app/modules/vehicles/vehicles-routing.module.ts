import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RepairesComponent} from "./pages/repaires/repaires.component";
import {VehiclesComponent} from "./pages/vehicles/vehicles.component";
import {NewRepaireComponent} from "./pages/repaires/new-repaire/new-repaire.component";
import {NewVehicleComponent} from "./pages/vehicles/new-vehicle/new-vehicle.component";
import {authGuard} from "../../services/guard/auth.guard";

const routes: Routes = [

  { path: 'all', component: VehiclesComponent,
        canActivate:[authGuard] },
  { path: 'new-vehicle', component: NewVehicleComponent,
        canActivate:[authGuard] },
  { path: 'edit-vehicle/:id', component: NewVehicleComponent,
        canActivate:[authGuard] },
  { path: 'repairs', component: RepairesComponent,
        canActivate:[authGuard] },
  { path: 'new-repair', component: NewRepaireComponent,
        canActivate:[authGuard] },
  { path: 'edit-repair/:id', component: NewRepaireComponent,
        canActivate:[authGuard] },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VehiclesRoutingModule { }
