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
import { ViewClientModalComponent } from './pages/clients/view-client-modal/view-client-modal.component';
import { NewDeliveryPointModalComponent } from './pages/clients/new-delivery-point-modal/new-delivery-point-modal.component';
import { ViewClientComponent } from './pages/clients/view-client/view-client.component';
import { NewDeliveryPointComponent } from './pages/clients/new-delivery-point/new-delivery-point.component';


@NgModule({
  declarations: [
    ClientsComponent,
    OrdersComponent,
    QuotesComponent,
    NewClientComponent,
    NewOrderComponent,
    NewQuoteComponent,
    ViewClientModalComponent,
    NewDeliveryPointModalComponent,
    ViewClientComponent,
    NewDeliveryPointComponent
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
