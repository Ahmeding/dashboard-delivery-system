import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DriversComponent} from "./pages/drivers/drivers.component";
import {ContractsComponent} from "./pages/contracts/contracts.component";
import {NewContractComponent} from "./pages/contracts/new-contract/new-contract.component";
import {NewDriverComponent} from "./pages/drivers/new-driver/new-driver.component";
import {authGuard} from "../../services/guard/auth.guard";

const routes: Routes = [
  { path: 'all', component: DriversComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'new-driver', component: NewDriverComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'edit-driver/:id', component: NewDriverComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'contracts', component: ContractsComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'new-contract', component: NewContractComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'edit-contract/:id', component: NewContractComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriversRoutingModule { }
