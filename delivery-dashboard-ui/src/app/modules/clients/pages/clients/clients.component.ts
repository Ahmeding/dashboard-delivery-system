import {Component, OnInit} from '@angular/core';
import {ClientDto} from "../../../../../ds-api/src/models/client-dto";
import {AdresseDto} from "../../../../../ds-api/src/models/adresse-dto";
import {PageResponseClientDto} from "../../../../../ds-api/src/models/page-response-client-dto";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {AlertService} from "../../../../services/alert/alert.service";
import {ClientService} from "../../../../../ds-api/src/services/client.service";
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent implements OnInit{

  allClients:ClientDto[]=[];
  selectedClient: ClientDto={};
  newClient: ClientDto={};

  isFormAddNewSubmitted: boolean=false;
  address: AdresseDto={};
  page: number=0;
  size: number=5;
  allClientsResponse: PageResponseClientDto = {};
  searchTerm: any;
  pageSize: any;

  constructor(
    private router:Router,
    private confirmBoxService:ConfirmBoxEvokeService,
    private alertService:AlertService,
    private clientService: ClientService) {
  }

  ngOnInit(): void {
    this.getAllClients();
  }
  private getAllClients() {
    this.clientService.findAllClients({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(clients)=>{
        this.allClientsResponse=clients;
      },
      error:(err)=>{
        console.log(err);
      }
    });
  }
  formatDateDDMMYYYY(datePMC: string | undefined) : string{
    const dateString= JSON.stringify(datePMC);
    const dateArray=JSON.parse(dateString);
    const year=dateArray[0];
    const month=dateArray[1].toString().padStart(2,'0');
    const day=dateArray[2].toString().padStart(2,'0');
    return `${day}-${month}-${year}`;
  }
  handleClientActivation(client_id: number | undefined, isActive: boolean | undefined) {
    const action = isActive? "disable" : "enable";
    if (client_id === undefined) {
      console.log("mission_id : "+ client_id);
      this.alertService.alertError('Mission ID is missing. Unable to delete mission.');
      return;
    }
    this.confirmBoxService.customFour(`Are you sure you want to ${action} this client?`, '', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.clientService.updateIsActive1(
            {
              id:client_id,
              isactive:!isActive
            }
          ).subscribe(() => {
            this.getAllClients();
            this.alertService.alertSuccess(`${action} successfully completed`);
          });
        }
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : `Unable to ${action} client`);
      }
    )
  }

  addNewClient(form: NgForm) {
    form.form.markAllAsTouched();
    this.isFormAddNewSubmitted= true;

    if(form.valid)
    {
      this.saveClient(this.newClient,form);
    }else{
      this.alertService.alertError('You must fill in the required fields.');
    }
  }

  private resetForm(form: NgForm) {
    form.resetForm();
    this.newClient={}
    this.isFormAddNewSubmitted=false;
  }


  deleteClient(client_id: number) {
    if (client_id === undefined) {
      this.alertService.alertError('Client ID is missing. Unable to delete client.');
      return;
    }
    this.confirmBoxService.customFour('Are you sure you want to delete this client?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.clientService.delete10({id:client_id}).subscribe(() => {
            this.getAllClients();
            this.alertService.alertSuccess('Deletion successfully completed');
          });
        }
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : 'Unable to delete client');
      }
    )
  }
  editClient(client_id: number | undefined) {
    this.clientService.findById11({
      id:client_id as number
    }).subscribe({
      next:(res)=>{
        this.selectedClient=res;
        this.address=this.selectedClient.address as AdresseDto;
        console.log(this.selectedClient);
      }
    })
  }
  convertDateToInput(dateArray: number[] ) :string{
    const year= dateArray[0];
    const month= dateArray[1].toString().padStart(2,'0');
    const day= dateArray[2].toString().padStart(2,'0');
    return `${year}-${month}-${day}`;
  }
  saveEditingClient(editForm: NgForm) {
    editForm.form.markAllAsTouched();
    this.isFormAddNewSubmitted= true;
    if(editForm.valid){
      this.saveClient(this.selectedClient,editForm);
    }else{
      this.alertService.alertError('You must fill in the required fields.');
    }

  }
  private saveClient(clientToSave: ClientDto, form: NgForm) {
    clientToSave.address = this.address;
    this.clientService.save11({
      body:clientToSave
    }).subscribe({
      next:(res)=>{
        console.log(res);
        this.alertService.alertSuccess("Client saved successfully!");
        this.getAllClients();
        this.resetForm(form);
      },
      error:(err)=>{
        console.log(err);
        this.alertService.alertError("An error occurred while saving a client.");
      }})
  }



  goToFirstPage() {
    this.page=0;
    this.getAllClients();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllClients();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllClients();
  }

  goToNextPage() {
    this.page++;
    this.getAllClients();
  }


  get  isLastPage():boolean{
    return this.page == this.allClientsResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allClientsResponse.totalPages as number -1;
  }

  filterClients() {

  }

  navigateToNewClient() {
    this.router.navigate(['clients','new-client']);
  }

  viewClientDetails(client: ClientDto) {

  }

  confirmDelete(client: ClientDto) {

  }

  onPageSizeChange() {

  }

  getPageNumbers() {
    return undefined;
  }
}
