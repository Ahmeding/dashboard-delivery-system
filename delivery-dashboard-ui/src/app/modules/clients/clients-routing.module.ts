import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ClientsComponent} from "./pages/clients/clients.component";
import {OrdersComponent} from "./pages/orders/orders.component";
import {QuotesComponent} from "./pages/quotes/quotes.component";

const routes: Routes = [
  { path: 'all', component: ClientsComponent },
  { path: 'orders', component: OrdersComponent },
  { path: 'quotes', component: QuotesComponent },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientsRoutingModule { }
