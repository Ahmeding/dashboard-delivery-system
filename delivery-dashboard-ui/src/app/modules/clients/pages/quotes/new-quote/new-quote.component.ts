import {Component, OnInit} from '@angular/core';
import {CommandeClientDto} from "../../../../../../ds-api/src/models/commande-client-dto";
import {
  PageResponseCommandeClientResponse
} from "../../../../../../ds-api/src/models/page-response-commande-client-response";
import {ClientDto} from "../../../../../../ds-api/src/models/client-dto";
import {ServiceDeliveryDto} from "../../../../../../ds-api/src/models/service-delivery-dto";
import {LigneCommandeClientDto} from "../../../../../../ds-api/src/models/ligne-commande-client-dto";
import {CommandeClientResponse} from "../../../../../../ds-api/src/models/commande-client-response";
import {CommandeClientService} from "../../../../../../ds-api/src/services/commande-client.service";
import {ClientService} from "../../../../../../ds-api/src/services/client.service";
import {ServiceDeliveryService} from "../../../../../../ds-api/src/services/service-delivery.service";
import {AlertService} from "../../../../../services/alert/alert.service";
import {UtilisateurService} from "../../../../../../ds-api/src/services/utilisateur.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-new-quote',
  templateUrl: './new-quote.component.html',
  styleUrls: ['./new-quote.component.scss']
})
export class NewQuoteComponent implements OnInit{

  testQuote: CommandeClientDto={
    code: "CMD0003",
    dateCommnde: "2024-10-23T07:15:13.717Z",
    clientId: 3,
    ligneCommandeClientsDtos: [

      {
        quantite: 2,
        prixUnitaire: 200,
        tva: 20,
        serviceDeliveryDtoId:  2

      },
      {
        quantite: 4,
        prixUnitaire: 700,
        tva: 20,
        serviceDeliveryDtoId : 3

      }
    ],
    etatCommande: "IN_PROGRESS"
  }
  page: number = 0;
  size: number =5;
  allQuotesResponse: PageResponseCommandeClientResponse={};
  newQuote: CommandeClientDto= {};
  selectedToEditQuote: CommandeClientDto= {};
  isFormAddNewSubmitted: boolean= false;
  allClients: ClientDto[]=[]

  allServiceDeliveries: ServiceDeliveryDto[]=[];
  selectedServiceDelivery: ServiceDeliveryDto|undefined={};
  allSelectedServiceDeliveries: ServiceDeliveryDto[]=[];

  orderLine:  LigneCommandeClientDto ={};
  orderlines: LigneCommandeClientDto[]=[];
  selectedQuote: CommandeClientResponse = {};
  isEditMode: boolean=false;


  constructor(
    private cmdClientService:CommandeClientService,
    private clientService:ClientService,
    private servDeliveryService: ServiceDeliveryService,
    private alertService: AlertService,
    private userService:UtilisateurService
  ) {}

  ngOnInit(): void {
    this.getAllQuotes();
    this.getAllClients();
    this.getAllServDelivery();
  }


  private getAllQuotes() {
    this.cmdClientService.findAllCommandeClients({
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
    this.newQuote.ligneCommandeClientsDtos=this.orderlines;
    this.newQuote.etatCommande= "IN_PROGRESS"
    console.log(this.newQuote);
    this.cmdClientService.save16({body:this.newQuote}).subscribe({
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
    const exists= this.orderlines.some(orderL=>orderL.serviceDeliveryDtoId===this.selectedServiceDelivery?.id)
    if(exists){
      this.alertService.alertWarning("This service has already been added.")
      return;
    }
    const newOrdeLine :LigneCommandeClientDto = {
      serviceDeliveryDtoId : this.selectedServiceDelivery.id,
      quantite: 2 as number,
      prixUnitaire: this.selectedServiceDelivery.basicTripCost,
      tva: 20,
    }

    this.orderLine.serviceDeliveryDtoId=this.selectedServiceDelivery.id;
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
    this.cmdClientService.findById16({id:id as number}).subscribe({
      next:(order)=>{
        this.selectedToEditQuote = order;
        this.orderlines = this.selectedToEditQuote.ligneCommandeClientsDtos as Array<CommandeClientDto>;
        console.log(this.selectedToEditQuote);
        const dateString = JSON.stringify(this.selectedQuote.dateCommnde);
        console.log("---------------"+this.convertDate(JSON.parse((dateString))));
        /* this.selectedToEditQuote.dateCommnde = this.convertDate(JSON.parse((dateString))) as Date;*/
      }
    })
  }

  saveEditingQuote(editForm: NgForm) {

  }
}
