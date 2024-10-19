import { NgModule } from '@angular/core';
import {CommonModule, NgForOf, NgIf} from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { MainComponent } from './pages/main/main.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { TopbarComponent } from './components/topbar/topbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { MainContentComponent } from './pages/main-content/main-content.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { RepairsComponent } from './pages/vehicles/repairs/repairs/repairs.component';
import { UsersComponent } from './pages/users/users/users.component';
import { ProfileComponent } from './pages/users/profile/profile.component';
import { NewContractComponent } from './pages/drivers/contracts/new-contract/new-contract.component';
import {ContractsComponent} from "./pages/drivers/contracts/contracts/contracts.component";
import { NewRepairComponent } from './pages/vehicles/repairs/new-repair/new-repair.component';
import { NewClientComponent } from './pages/clients/clients/new-client/new-client.component';
import {ClientsComponent} from "./pages/clients/clients/clients/clients.component";
import { NewOrderComponent } from './pages/clients/orders/new-order/new-order.component';
import {OrdersComponent} from "./pages/clients/orders/orders/orders.component";
import {QuotesComponent} from "./pages/clients/quotes/quotes/quotes.component";
import { NewQuoteComponent } from './pages/clients/quotes/new-quote/new-quote.component';
import { NewExpenseComponent } from './pages/missions/expenses/new-expense/new-expense.component';
import {NewMissionComponent} from "./pages/missions/missions/new-mission/new-mission.component";
import {ExpensesComponent} from "./pages/missions/expenses/expenses/expenses.component";
import {MissionsComponent} from "./pages/missions/missions/missions/missions.component";
import {NewVehicleComponent} from "./pages/vehicles/vehicles/new-vehicle/new-vehicle.component";
import {VehiclesComponent} from "./pages/vehicles/vehicles/vehicles/vehicles.component";
import {DriversComponent} from "./pages/drivers/drivers/drivers/drivers.component";
import {NewDriverComponent} from "./pages/drivers/drivers/new-driver/new-driver.component";
import {NgxPaginationModule} from "ngx-pagination";
import {MissionDetailsComponent} from "./pages/missions/missions/mission-details/mission-details.component";
import { AddMissionComponent } from './pages/missions/missions/add-mission/add-mission.component';
import {MultiStepFormComponent} from "./components/multi-step-form/multi-step-form.component";


@NgModule({
  declarations: [
    MainComponent,
    SidebarComponent,
    TopbarComponent,
    FooterComponent,
    MainContentComponent,
    NewMissionComponent,
    DriversComponent,
    NewDriverComponent,
    NewVehicleComponent,
    VehiclesComponent,
    ExpensesComponent,
    MissionsComponent,
    ContractsComponent,
    RepairsComponent,
    ClientsComponent,
    OrdersComponent,
    QuotesComponent,
    UsersComponent,
    ProfileComponent,
    NewContractComponent,
    NewRepairComponent,
    NewClientComponent,
    NewOrderComponent,
    NewQuoteComponent,
    NewQuoteComponent,
    NewExpenseComponent,
    MissionDetailsComponent,
    AddMissionComponent,
    MultiStepFormComponent,
  ],
    imports: [
        CommonModule,
        DashboardRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        FormsModule,
        ReactiveFormsModule,
        FormsModule,
        NgForOf,
        NgIf,
        ReactiveFormsModule,
        FormsModule,
        ReactiveFormsModule,
        NgxPaginationModule
    ]
})
export class DashboardModule { }
