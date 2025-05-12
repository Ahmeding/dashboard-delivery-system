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
import {NewDeliveryPointComponent} from "./pages/clients/new-delivery-point/new-delivery-point.component";

const routes: Routes = [
  {
    path: 'all',
    component: ClientsComponent,
    canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'new-client', component: NewClientComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'view-client/:id', component: ViewClientComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'new-delivery-points/:id', component: NewDeliveryPointComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'edit-client/:id', component: NewClientComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'orders', component: OrdersComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'new-order', component: NewOrderComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'quotes', component: QuotesComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },
  { path: 'new-quote', component: NewQuoteComponent,
        canActivate:[authGuard],
    data: {roles: ['ROLE_ADMIN']} },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientsRoutingModule { }
