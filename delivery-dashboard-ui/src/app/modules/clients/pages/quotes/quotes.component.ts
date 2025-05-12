import {Component, OnInit} from '@angular/core';
import {
  PageResponseCommandeClientResponse
} from "../../../../../ds-api/src/models/page-response-commande-client-response";
import {CommandeClientDto} from "../../../../../ds-api/src/models/commande-client-dto";
import {ClientDto} from "../../../../../ds-api/src/models/client-dto";
import {ServiceDeliveryDto} from "../../../../../ds-api/src/models/service-delivery-dto";
import {LigneCommandeClientDto} from "../../../../../ds-api/src/models/ligne-commande-client-dto";
import {CommandeClientResponse} from "../../../../../ds-api/src/models/commande-client-response";
import {CommandeClientService} from "../../../../../ds-api/src/services/commande-client.service";
import {ClientService} from "../../../../../ds-api/src/services/client.service";
import {ServiceDeliveryService} from "../../../../../ds-api/src/services/service-delivery.service";
import {AlertService} from "../../../../services/alert/alert.service";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {DevisDto} from "../../../../../ds-api/src/models/devis-dto";
import {PageResponseDevisDtoResponse} from "../../../../../ds-api/src/models/page-response-devis-dto-response";
import {LigneDevisDto} from "../../../../../ds-api/src/models/ligne-devis-dto";
import {DevisDtoResponse} from "../../../../../ds-api/src/models/devis-dto-response";
import {DevisService} from "../../../../../ds-api/src/services/devis.service";

@Component({
  selector: 'app-quotes',
  templateUrl: './quotes.component.html',
  styleUrls: ['./quotes.component.scss']
})
export class QuotesComponent implements OnInit{

  page: number = 0;
  size: number =5;
  allQuotesResponse: PageResponseDevisDtoResponse={};
  newQuote: DevisDto= {};
  selectedToEditOrder: CommandeClientDto= {};
  selectedToEditQuote: DevisDto= {};
  isFormAddNewSubmitted: boolean= false;
  allClients: ClientDto[]=[]

  allServiceDeliveries: ServiceDeliveryDto[]=[];
  selectedServiceDelivery: ServiceDeliveryDto|undefined={};
  allSelectedServiceDeliveries: ServiceDeliveryDto[]=[];

  orderLine:  LigneDevisDto ={};
  orderlines: LigneDevisDto[]=[];
  selectedQuote: DevisDtoResponse = {};


  constructor(
    private devisService:DevisService,
    private clientService:ClientService,
    private servDeliveryService: ServiceDeliveryService,
    private alertService: AlertService,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.getAllQuotes();
    this.getAllClients();
    this.getAllServDelivery();
  }


  private getAllQuotes() {
    this.devisService.findAllDevis({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(orders)=>{
        console.log(orders);
        this.allQuotesResponse=orders;
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
    this.clientService.findAll17().subscribe({
      next:(clientsResponse)=>{
        this.allClients=clientsResponse;
      }
    })
  }

  addNewQuote(form: NgForm) {

    this.isFormAddNewSubmitted=true;
    form.form.markAllAsTouched();
    this.newQuote.ligneDevisDtos=this.orderlines;
    this.newQuote.etatDevis= "PENDING_APPROVAL"
    console.log(this.newQuote);
    this.devisService.save11({body:this.newQuote}).subscribe({
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
    this.newQuote = {};   // Reset the driver object to its initial state
    this.isFormAddNewSubmitted = false; // Reset the submitted state
  }

  private getAllServDelivery() {
    this.servDeliveryService.findAll3().subscribe({
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
    const exists= this.orderlines.some(orderL=>orderL.serviceDeliveryDto?.id===this.selectedServiceDelivery?.id)
    if(exists){
      this.alertService.alertWarning("This service has already been added.")
      return;
    }
    const newOrdeLine :LigneDevisDto = {
      serviceDeliveryDto : this.selectedServiceDelivery,
      quantite: 2 as number,
      prixUnitaire: this.selectedServiceDelivery.basicTripCost,
      tva: 20,
    }

    this.orderLine.serviceDeliveryDto=this.selectedServiceDelivery;
    this.orderlines.push(newOrdeLine);
    this.selectedServiceDelivery = undefined;
  }

  deleteServDelivery(i: number) {
    this.orderlines.splice(i,1);
  }

  onQteChange(event: any, index: number) {
    const selectedQte = event.target.value as number;
    this.orderlines[index].quantite=selectedQte;

  }

  calculTotalLigne(quantite: number | undefined, prixTTC: number | undefined):number {

    const validQuantite = quantite ?? 0; // Default to 0 if undefined
    const validPrixTTC = prixTTC ?? 0; // Default to 0 if undefined

    return validQuantite * validPrixTTC  ;
  }

  editQuote(id: number | undefined) {
    this.devisService.findById11({id:id as number}).subscribe({
      next:(quote)=>{
        this.selectedToEditQuote = quote as DevisDto;
        this.orderlines = this.selectedToEditQuote.ligneDevisDtos as Array<DevisDto>;
        console.log(this.selectedToEditQuote);
        const dateString = JSON.stringify(this.selectedQuote.dateDevis);
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
