import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClientsRoutingModule } from './clients-routing.module';
import { OrdersComponent } from './pages/orders/orders.component';
import { QuotesComponent } from './pages/quotes/quotes.component';
import {ClientsComponent} from "./pages/clients/clients.component";


@NgModule({
  declarations: [
    ClientsComponent,
    OrdersComponent,
    QuotesComponent
  ],
  imports: [
    CommonModule,
    ClientsRoutingModule
  ]
})
export class ClientsModule { }
