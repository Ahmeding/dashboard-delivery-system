import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DriversComponent} from "./pages/drivers/drivers.component";
import {ContractsComponent} from "./pages/contracts/contracts.component";
import {NewContractComponent} from "./pages/contracts/new-contract/new-contract.component";
import {NewDriverComponent} from "./pages/drivers/new-driver/new-driver.component";

const routes: Routes = [
  { path: 'all', component: DriversComponent },
  { path: 'new-driver', component: NewDriverComponent },
  { path: 'edit-driver/:id', component: NewDriverComponent },
  { path: 'contracts', component: ContractsComponent },
  { path: 'new-contract', component: NewContractComponent },
  { path: 'edit-contract/:id', component: NewContractComponent },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriversRoutingModule { }
