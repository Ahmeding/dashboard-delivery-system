import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ClientsComponent} from "./pages/clients/clients.component";
import {OrdersComponent} from "./pages/orders/orders.component";
import {QuotesComponent} from "./pages/quotes/quotes.component";
import {NewClientComponent} from "./pages/clients/new-client/new-client.component";
import {NewOrderComponent} from "./pages/orders/new-order/new-order.component";
import {NewQuoteComponent} from "./pages/quotes/new-quote/new-quote.component";
import {ViewClientComponent} from "./pages/clients/view-client/view-client.component";

const routes: Routes = [
  { path: 'all', component: ClientsComponent },
  { path: 'new-client', component: NewClientComponent },
  { path: 'view-client/:id', component: ViewClientComponent },
  { path: 'edit-client/:id', component: NewClientComponent },
  { path: 'orders', component: OrdersComponent },
  { path: 'new-order', component: NewOrderComponent },
  { path: 'quotes', component: QuotesComponent },
  { path: 'new-quote', component: NewQuoteComponent },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientsRoutingModule { }
