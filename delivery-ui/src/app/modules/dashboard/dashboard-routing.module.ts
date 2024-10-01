import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./pages/main/main.component";
import {MainContentComponent} from "./pages/main-content/main-content.component";
import {RepairsComponent} from "./pages/vehicles/repairs/repairs/repairs.component";
import {UsersComponent} from "./pages/users/users/users.component";
import {ProfileComponent} from "./pages/users/profile/profile.component";
import {ContractsComponent} from "./pages/drivers/contracts/contracts/contracts.component";
import {ClientsComponent} from "./pages/clients/clients/clients/clients.component";
import {NewClientComponent} from "./pages/clients/clients/new-client/new-client.component";
import {OrdersComponent} from "./pages/clients/orders/orders/orders.component";
import {QuotesComponent} from "./pages/clients/quotes/quotes/quotes.component";
import {NewOrderComponent} from "./pages/clients/orders/new-order/new-order.component";
import {NewQuoteComponent} from "./pages/clients/quotes/new-quote/new-quote.component";
import {NewMissionComponent} from "./pages/missions/missions/new-mission/new-mission.component";
import {MissionsComponent} from "./pages/missions/missions/missions/missions.component";
import {ExpensesComponent} from "./pages/missions/expenses/expenses/expenses.component";
import {NewExpenseComponent} from "./pages/missions/expenses/new-expense/new-expense.component";
import {VehiclesComponent} from "./pages/vehicles/vehicles/vehicles/vehicles.component";
import {NewVehicleComponent} from "./pages/vehicles/vehicles/new-vehicle/new-vehicle.component";
import {DriversComponent} from "./pages/drivers/drivers/drivers/drivers.component";
import {NewDriverComponent} from "./pages/drivers/drivers/new-driver/new-driver.component";
import {authGuard} from "../../services/guard/auth.guard";
import {MissionDetailsComponent} from "./pages/missions/missions/mission-details/mission-details.component";

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        component: MainContentComponent,
        canActivate: [authGuard]
      }, {
        path: 'new-mission',
        component: NewMissionComponent,
        canActivate: [authGuard]
      }, {
        path: 'missions',
        component: MissionsComponent,
        canActivate: [authGuard]
      },{
        path: 'details/:id',
        component: MissionDetailsComponent,
        canActivate: [authGuard]
      },
      {
        path: 'expenses',
        component: ExpensesComponent,
        canActivate: [authGuard]
      },
      {
        path: 'new-expense',
        component: NewExpenseComponent,
    canActivate: [authGuard]
      }, {
        path: 'drivers',
        component: DriversComponent,
      },
      {
        path: 'new-driver',
        component: NewDriverComponent,
    canActivate: [authGuard]
      },
      {
        path: 'contracts',
        component: ContractsComponent,
    canActivate: [authGuard]
      }, {
        path: 'vehicles',
        component: VehiclesComponent,
       canActivate: [authGuard]
      }, {
        path: 'new-vehicle',
        component: NewVehicleComponent,
        canActivate: [authGuard]
      }, {
        path: 'repairs',
        component: RepairsComponent,
    canActivate: [authGuard]
      },
      {
        path: 'clients',
        component: ClientsComponent,
    canActivate: [authGuard]
      },
      {
        path: 'new-client',
        component: NewClientComponent,
    canActivate: [authGuard]
      },
      {
        path: 'orders',
        component: OrdersComponent,
    canActivate: [authGuard]
      },
      {
        path: 'new-order',
        component: NewOrderComponent,
    canActivate: [authGuard]
      },
      {
        path: 'quotes',
        component: QuotesComponent,
    canActivate: [authGuard]
      },
      {
        path: 'new-quote',
        component: NewQuoteComponent,
    canActivate: [authGuard]
      },
      {
        path: 'users',
        component: UsersComponent,
    canActivate: [authGuard]
      },
      {
        path: 'profile',
        component: ProfileComponent,
    canActivate: [authGuard]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule {
}
