import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DriversComponent} from "./pages/drivers/drivers.component";
import {ContractsComponent} from "./pages/contracts/contracts.component";
import {NewContractComponent} from "./pages/contracts/new-contract/new-contract.component";

const routes: Routes = [
  { path: 'all', component: DriversComponent },
  { path: 'contracts', component: ContractsComponent },
  { path: 'new-contract', component: NewContractComponent },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriversRoutingModule { }
