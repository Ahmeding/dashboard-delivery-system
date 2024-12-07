import {Component, OnInit} from '@angular/core';
import {ClientDto} from "../../../../../ds-api/src/models/client-dto";
import {AdresseDto} from "../../../../../ds-api/src/models/adresse-dto";
import {PageResponseClientDto} from "../../../../../ds-api/src/models/page-response-client-dto";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {AlertService} from "../../../../services/alert/alert.service";
import {ClientService} from "../../../../../ds-api/src/services/client.service";
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {ViewClientModalComponent} from "./view-client-modal/view-client-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DeliveryPointDto} from "../../../../../ds-api/src/models/delivery-point-dto";
import {NewDeliveryPointModalComponent} from "./new-delivery-point-modal/new-delivery-point-modal.component";

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent implements OnInit{

  deliveryPoint:DeliveryPointDto = {}
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
    private modalService: NgbModal,
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

  handleClientActivation(client_id: number | undefined, isActive: boolean | undefined) {
    const action = isActive? "disable" : "enable";
    const updatedStatus = !isActive;
    if (client_id === undefined) {
      console.log("mission_id : "+ client_id);
      this.alertService.alertError('Mission ID is missing. Unable to deactivate Client.');
      return;
    }
    this.confirmBoxService.customFour(`Are you sure you want to ${action} this client?`, '', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.clientService.updateIsActive2(
            {
              id:client_id,
              isactive:updatedStatus
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

  deleteClient(client_id: number) {
    if (client_id === undefined) {
      this.alertService.alertError('Client ID is missing. Unable to delete client.');
      return;
    }
    this.confirmBoxService.customFour('Are you sure you want to delete this client?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.clientService.delete14({id:client_id}).subscribe(() => {
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
    const modalRef = this.modalService.open(ViewClientModalComponent, { size: 'xl' });
    modalRef.componentInstance.selectedClient = client;  // Pass data to modal
  }


  onPageSizeChange() {

  }

  getPageNumbers() {
    const totalPages = this.allClientsResponse?.totalPages as number -1 || 0;
    return undefined;//Array.from({ length : totalPages }, (_, i) => i + 1);
  }

  goToEditClient(id: number | undefined) {
    this.router.navigate(['clients','edit-client',id]);
  }

  openAddNewDPModal(client: any) {
    const modalRef = this.modalService.open(NewDeliveryPointModalComponent, {
      size: 'lg',
      centered: true,
      backdrop: 'static'
    });

    modalRef.componentInstance.clientId = client.id;

    modalRef.result.then((result) => {
      if (result) {
        // Handle the new delivery point data
        console.log('New delivery point:', result);
        this.getAllClients();
        // Add your API call here
      }
    }).catch((error) => {
      console.log('Modal dismissed');
    });
  }

  goToClientDetails(id: number | undefined) {
    this.router.navigate(['clients','view-client',id]);
  }
}
