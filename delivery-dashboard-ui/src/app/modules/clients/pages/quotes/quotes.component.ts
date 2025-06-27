import {Component, OnInit} from '@angular/core';

import {ClientService} from "../../../../../ds-api/src/services/client.service";
import {AlertService} from "../../../../services/alert/alert.service";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {PageResponseQuoteResponse} from "../../../../../ds-api/src/models/page-response-quote-response";
import {QuoteRequest} from "../../../../../ds-api/src/models/quote-request";
import {ClientResponse} from "../../../../../ds-api/src/models/client-response";
import {DeliveryServiceResponse} from "../../../../../ds-api/src/models/delivery-service-response";
import {QuoteLineRequest} from "../../../../../ds-api/src/models/quote-line-request";
import {QuoteResponse} from "../../../../../ds-api/src/models/quote-response";
import {QuoteService} from "../../../../../ds-api/src/services/quote.service";
import {DeliveryServiceService} from "../../../../../ds-api/src/services/delivery-service.service";
import {QuoteLineResponse} from "../../../../../ds-api/src/models/quote-line-response";

@Component({
  selector: 'app-quotes',
  templateUrl: './quotes.component.html',
  styleUrls: ['./quotes.component.scss']
})
export class QuotesComponent implements OnInit{

  page: number = 0;
  size: number =5;
  allQuotesResponse: PageResponseQuoteResponse={};
  newQuote!: QuoteRequest;
  selectedToEditQuote: QuoteResponse={};
  isFormAddNewSubmitted: boolean= false;
  allClients: ClientResponse[]=[]

  allServiceDeliveries: DeliveryServiceResponse[]=[];
  selectedServiceDelivery: DeliveryServiceResponse|undefined={};

  orderLine!:  QuoteLineRequest;
  quotelines: QuoteLineRequest[]=[];
  selectedQuote!: QuoteResponse;
  private quotelinesResp!: Array<QuoteResponse>;


  constructor(
    private quoteService:QuoteService,
    private clientService:ClientService,
    private deliveryServiceService: DeliveryServiceService,
    private alertService: AlertService,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.getAllQuotes();
    this.getAllClients();
    this.getAllServDelivery();
  }


  private getAllQuotes() {
    this.quoteService.findAllQuotesPageable({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(pageableQuotes)=>{
        console.log(pageableQuotes);
        this.allQuotesResponse=pageableQuotes;
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
    this.getAllQuotes();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllQuotes();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllQuotes();
  }

  goToNextPage() {
    this.page++;
    this.getAllQuotes();
  }


  get  isLastPage():boolean{
    return this.page == this.allQuotesResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allQuotesResponse.totalPages as number -1;
  }

  private getAllClients() {
    this.clientService.findAllClient().subscribe({
      next:(clientsResponse)=>{
        this.allClients=clientsResponse;
      }
    })
  }

  addNewQuote(form: NgForm) {

    this.isFormAddNewSubmitted=true;
    form.form.markAllAsTouched();
    this.newQuote.quoteLines=this.quotelines;
    this.newQuote.quoteStatus= "PENDING_APPROVAL"
    console.log(this.newQuote);
    this.quoteService.saveQuote({body:this.newQuote}).subscribe({
      next:(resp)=>{
        this.getAllQuotes();
        this.resetForm(form);
        this.alertService.alertInfo("Quote saved successfully!");
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
    this.newQuote!;   // Reset the driver object to its initial state
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
    const exists= this.quotelines.some(orderL=>orderL.deliveryServiceId===this.selectedServiceDelivery?.id)
    if(exists){
      this.alertService.alertWarning("This service has already been added.")
      return;
    }
    const newOrdeLine :QuoteLineRequest = {
      deliveryServiceId : this.selectedServiceDelivery.id,
      quantity: 2 as number,
      unitPrice: this.selectedServiceDelivery.basicTripCost as number,
      tva: 20,
      quoteId:0,
      ttcPrice:0
    }

    this.orderLine.deliveryServiceId=this.selectedServiceDelivery.id;
    this.quotelines.push(newOrdeLine);
    this.selectedServiceDelivery = undefined;
  }

  deleteServDelivery(i: number) {
    this.quotelines.splice(i,1);
  }

  onQteChange(event: any, index: number) {
    const selectedQte = event.target.value as number;
    this.quotelines[index].quantity=selectedQte;

  }

  calculTotalLigne(quantite: number | undefined, prixTTC: number | undefined):number {

    const validQuantite = quantite ?? 0; // Default to 0 if undefined
    const validPrixTTC = prixTTC ?? 0; // Default to 0 if undefined

    return validQuantite * validPrixTTC  ;
  }

  editQuote(id: number | undefined) {
    this.quoteService.findQuoteById({id:id as number}).subscribe({
      next:(quote)=>{
        this.selectedToEditQuote = quote as QuoteResponse;
        this.quotelinesResp = this.selectedToEditQuote.quoteLines as Array<QuoteResponse>;
        console.log(this.selectedToEditQuote);
        const dateString = JSON.stringify(this.selectedQuote.quoteDate);
        console.log("---------------"+this.convertDate(JSON.parse((dateString))));
        /* this.selectedToEditQuote.dateCommnde = this.convertDate(JSON.parse((dateString))) as Date;*/
      }
    })
  }

  saveEditingQuote(editForm: NgForm) {

  }

  navigateToNewQuote() {
    this.router.navigate(['clients','new-quote'])
  }
}
