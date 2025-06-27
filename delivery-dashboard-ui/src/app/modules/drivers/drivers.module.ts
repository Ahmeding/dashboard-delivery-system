import { NgModule } from '@angular/core';
import {CommonModule, NgForOf, NgIf} from '@angular/common';

import { DriversRoutingModule } from './drivers-routing.module';
import { ContractsComponent } from './pages/contracts/contracts.component';
import {DriversComponent} from "./pages/drivers/drivers.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NewContractComponent } from './pages/contracts/new-contract/new-contract.component';
import { NewDriverComponent } from './pages/drivers/new-driver/new-driver.component';
import { ViewDriverModalComponent } from './pages/drivers/view-driver-modal/view-driver-modal.component';
import { ViewContractModalComponent } from './pages/contracts/view-contract-modal/view-contract-modal.component';


@NgModule({
    declarations: [
        DriversComponent,
        ContractsComponent,
        NewContractComponent,
        NewDriverComponent,
        ViewDriverModalComponent,
        ViewContractModalComponent
    ],
    exports: [
        DriversComponent
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
