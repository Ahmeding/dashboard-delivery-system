import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RepairesComponent} from "./pages/repaires/repaires.component";
import {VehiclesComponent} from "./pages/vehicles/vehicles.component";
import {NewRepaireComponent} from "./pages/repaires/new-repaire/new-repaire.component";
import {NewVehicleComponent} from "./pages/vehicles/new-vehicle/new-vehicle.component";
import {authGuard} from "../../services/guard/auth.guard";

const routes: Routes = [

  { path: 'all', component: VehiclesComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'new-vehicle', component: NewVehicleComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'edit-vehicle/:id', component: NewVehicleComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'repairs', component: RepairesComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'new-repair', component: NewRepaireComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'edit-repair/:id', component: NewRepaireComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VehiclesRoutingModule { }
