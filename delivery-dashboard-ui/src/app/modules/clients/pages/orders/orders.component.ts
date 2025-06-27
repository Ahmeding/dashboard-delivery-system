import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {AlertService} from "../../../../services/alert/alert.service";
import {ClientService} from "../../../../../ds-api/src/services/client.service";
import {Router} from "@angular/router";
import {PageResponseQuoteResponse} from "../../../../../ds-api/src/models/page-response-quote-response";
import {OrderRequest} from "../../../../../ds-api/src/models/order-request";
import {OrderResponse} from "../../../../../ds-api/src/models/order-response";
import {ClientResponse} from "../../../../../ds-api/src/models/client-response";
import {DeliveryServiceResponse} from "../../../../../ds-api/src/models/delivery-service-response";
import {OrderLineRequest} from "../../../../../ds-api/src/models/order-line-request";
import {OrderService} from "../../../../../ds-api/src/services/order.service";
import {DeliveryServiceService} from "../../../../../ds-api/src/services/delivery-service.service";
import {PageResponseOrderResponse} from "../../../../../ds-api/src/models/page-response-order-response";
import {OrderLineResponse} from "../../../../../ds-api/src/models/order-line-response";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit{

  page: number = 0;
  size: number =5;
  allOrdersResponse: PageResponseOrderResponse={};
  newOrder!: OrderRequest;
  selectedToEditOrder: OrderResponse= {};
  isFormAddNewSubmitted: boolean= false;
  allClients: ClientResponse[]=[]

  allServiceDeliveries: DeliveryServiceResponse[]=[];
  selectedServiceDelivery: DeliveryServiceResponse|undefined={};

  orderLine!:  OrderLineRequest;
  orderlines: OrderLineRequest[]=[];
  selectedOrder: OrderResponse = {};


  constructor(
    private orderService:OrderService,
    private clientService:ClientService,
    private deliveryServiceService: DeliveryServiceService,
    private alertService: AlertService,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.getAllOrders();
    this.getAllClients();
    this.getAllServDelivery();
  }


  private getAllOrders() {
    this.orderService.findAllOrdersPageable({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(ordersPageable)=>{
        console.log(ordersPageable);
        this.allOrdersResponse=ordersPageable;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }


  converteDateTime(dateCommnde: string | undefined):string {
    const dateTimeString = JSON.stringify(dateCommnde);
    const dateTimeToArray: number[] = JSON.parse(dateTimeString);

    return this.convertToDateTimeMinutes(dateTimeToArray).toString();
  }



  private convertToDateTimeMinutes(dateTimeToArray: number[]) :Date {
    const [year,month,day,hour,minute]= dateTimeToArray;
    const date = new Date(year, month-1, day, hour, minute);
    return date;
  }

  convertDate(dateArray: number[] ) :Date | null{
    if (!Array.isArray(dateArray) || dateArray.length < 5) return null;

    const [year, month, day, hour, minute] = dateArray;
    return new Date(year, month - 1, day, hour, minute);

  }


  goToFirstPage() {
    this.page=0;
    this.getAllOrders();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllOrders();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllOrders();
  }

  goToNextPage() {
    this.page++;
    this.getAllOrders();
  }


  get  isLastPage():boolean{
    return this.page == this.allOrdersResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allOrdersResponse.totalPages as number -1;
  }

  private getAllClients() {
    this.clientService.findAllClient().subscribe({
      next:(clientsResponse)=>{
        this.allClients=clientsResponse;
      }
    })
  }

  addNewOrder(form: NgForm) {

    this.isFormAddNewSubmitted=true;
    form.form.markAllAsTouched();
    this.newOrder.orderLines=this.orderlines;
    this.newOrder.orderStatus= "IN_PROGRESS"
    console.log(this.newOrder);
    this.orderService.saveOrder({body:this.newOrder}).subscribe({
      next:(resp)=>{
        this.getAllOrders();
        this.resetForm(form);
        this.alertService.alertInfo("Order saved successfully!");
        setTimeout(()=>{
          window.location.reload()
        },10)
      },error:(err)=>{
        this.alertService.alertError("An error occurred while saving an order")
      }
    })
  }
  private resetForm(form: NgForm) {
    form.resetForm();  // Reset the form state and clear input values
    this.newOrder!;   // Reset the driver object to its initial state
    this.isFormAddNewSubmitted = false; // Reset the submitted state
  }

  private getAllServDelivery() {
    this.deliveryServiceService.findAllDeliveryService().subscribe({
      next:(allSDResponse)=>{
        this.allServiceDeliveries=allSDResponse;
      },
      error:(err)=>{
        console.log(err);
      }})
  }


  onServiceDeliveryChange() {
    console.log(this.selectedServiceDelivery);
    //this.orderLine.prixUnitaire=this.selectedServiceDelivery?.unitPriceHT;

  }

  addServDelivery() {

    this.isFormAddNewSubmitted=true;

    if (!this.selectedServiceDelivery || !this.selectedServiceDelivery.id) {
      this.alertService.alertWarning("Please select a service before adding")
      return;
    }
    const exists= this.orderlines.some(orderL=>orderL.deliveryServiceId===this.selectedServiceDelivery?.id)
    if(exists){
      this.alertService.alertWarning("This service has already been added.")
      return;
    }
    const newOrderLine :OrderLineRequest = {
      deliveryServiceId : this.selectedServiceDelivery.id,
      quantity: 2 as number,
      unitPrice: this.selectedServiceDelivery.basicTripCost as number,
      tva: 20,
      ttcPrice:0,
      orderId: this.selectedOrder.id as number

    }

    this.orderLine.deliveryServiceId=this.selectedServiceDelivery.id;
    this.orderlines.push(newOrderLine);
    this.selectedServiceDelivery = undefined;
  }

  deleteServDelivery(i: number) {
    this.orderlines.splice(i,1);
  }

  onQteChange(event: any, index: number) {
    const selectedQte = event.target.value as number;
    this.orderlines[index].quantity=selectedQte;

  }

  calculTotalLigne(quantite: number | undefined, prixTTC: number | undefined):number {

    const validQuantite = quantite ?? 0; // Default to 0 if undefined
    const validPrixTTC = prixTTC ?? 0; // Default to 0 if undefined

    return validQuantite * validPrixTTC  ;
  }

  editOrder(id: number | undefined) {
    this.orderService.findOrderById({id:id as number}).subscribe({
      next:(order)=>{
        this.selectedToEditOrder = order;
        this.orderlines = this.selectedToEditOrder.orderLines as Array<OrderLineRequest>;
        console.log(this.selectedToEditOrder);
        const dateString = JSON.stringify(this.selectedOrder.orderDate);
        console.log("---------------"+this.convertDate(JSON.parse((dateString))));
        /* this.selectedToEditOrder.dateCommnde = this.convertDate(JSON.parse((dateString))) as Date;*/
      }
    })
  }

  saveEditingOrder(editForm: NgForm) {

  }

  navigateToNewOrder() {
    this.router.navigate(['clients','new-order'])
  }
}
