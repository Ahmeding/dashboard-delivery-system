import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ClientsComponent} from "./pages/clients/clients.component";
import {OrdersComponent} from "./pages/orders/orders.component";
import {QuotesComponent} from "./pages/quotes/quotes.component";
import {NewClientComponent} from "./pages/clients/new-client/new-client.component";
import {NewOrderComponent} from "./pages/orders/new-order/new-order.component";
import {NewQuoteComponent} from "./pages/quotes/new-quote/new-quote.component";
import {ViewClientComponent} from "./pages/clients/view-client/view-client.component";
import {authGuard} from "../../services/guard/auth.guard";

const routes: Routes = [
  { path: 'all', component: ClientsComponent,canActivate:[authGuard] },
  { path: 'new-client', component: NewClientComponent,
        canActivate:[authGuard] },
  { path: 'view-client/:id', component: ViewClientComponent,
        canActivate:[authGuard] },
  { path: 'edit-client/:id', component: NewClientComponent,
        canActivate:[authGuard] },
  { path: 'orders', component: OrdersComponent,
        canActivate:[authGuard] },
  { path: 'new-order', component: NewOrderComponent,
        canActivate:[authGuard] },
  { path: 'quotes', component: QuotesComponent,
        canActivate:[authGuard] },
  { path: 'new-quote', component: NewQuoteComponent,
        canActivate:[authGuard] },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientsRoutingModule { }
