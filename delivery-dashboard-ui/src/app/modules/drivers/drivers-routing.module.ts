import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DriversComponent} from "./pages/drivers/drivers.component";
import {ContractsComponent} from "./pages/contracts/contracts.component";

const routes: Routes = [
  { path: 'all', component: DriversComponent },
  { path: 'contracts', component: ContractsComponent },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriversRoutingModule { }
