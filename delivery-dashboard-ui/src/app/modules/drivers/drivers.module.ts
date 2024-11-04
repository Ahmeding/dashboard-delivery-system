import { NgModule } from '@angular/core';
import {CommonModule, NgForOf, NgIf} from '@angular/common';

import { DriversRoutingModule } from './drivers-routing.module';
import { ContractsComponent } from './pages/contracts/contracts.component';
import {DriversComponent} from "./pages/drivers/drivers.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NewContractComponent } from './pages/contracts/new-contract/new-contract.component';


@NgModule({
  declarations: [
    DriversComponent,
    ContractsComponent,
    NewContractComponent
  ],
    imports: [
        CommonModule,
        DriversRoutingModule,
        FormsModule,
        NgForOf,
        NgIf,
        ReactiveFormsModule
    ]
})
export class DriversModule { }
