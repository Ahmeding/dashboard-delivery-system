import { NgModule } from '@angular/core';
import {CommonModule, DatePipe, NgForOf, NgIf} from '@angular/common';

import { ClientsRoutingModule } from './clients-routing.module';
import { OrdersComponent } from './pages/orders/orders.component';
import { QuotesComponent } from './pages/quotes/quotes.component';
import {ClientsComponent} from "./pages/clients/clients.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NewClientComponent } from './pages/clients/new-client/new-client.component';
import { NewOrderComponent } from './pages/orders/new-order/new-order.component';
import { NewQuoteComponent } from './pages/quotes/new-quote/new-quote.component';


@NgModule({
  declarations: [
    ClientsComponent,
    OrdersComponent,
    QuotesComponent,
    NewClientComponent,
    NewOrderComponent,
    NewQuoteComponent
  ],
  imports: [
    CommonModule,
    ClientsRoutingModule,
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    DatePipe,
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule
  ]
})
export class ClientsModule { }
