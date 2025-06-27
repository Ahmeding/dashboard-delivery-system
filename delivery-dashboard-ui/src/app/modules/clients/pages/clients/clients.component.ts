import {Component, OnInit} from '@angular/core';
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {AlertService} from "../../../../services/alert/alert.service";
import {ClientService} from "../../../../../ds-api/src/services/client.service";
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {ViewClientModalComponent} from "./view-client-modal/view-client-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NewDeliveryPointModalComponent} from "./new-delivery-point-modal/new-delivery-point-modal.component";
import {Client} from "stompjs";
import {TokenService} from "../../../../services/token/token.service";
import {DeliveryPointRequest} from "../../../../../ds-api/src/models/delivery-point-request";
import {ClientResponse} from "../../../../../ds-api/src/models/client-response";
import {AddressRequest} from "../../../../../ds-api/src/models/address-request";
import {PageResponseClientResponse} from "../../../../../ds-api/src/models/page-response-client-response";

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent implements OnInit{

  selectedClient: ClientResponse={};

  isFormAddNewSubmitted: boolean=false;
  address!: AddressRequest;
  page: number=0;
  size: number=5;
  allClientsResponse: PageResponseClientResponse = {};
  searchTerm: any;
  pageSize: any;
// Inside your component class
  private activeModalRef: any;
  constructor(
    private tokenService:TokenService,
    private modalService: NgbModal,
    private router:Router,
    private confirmBoxService:ConfirmBoxEvokeService,
    private alertService:AlertService,
    private clientService: ClientService) {
    // Subscribe to token changes

    this.tokenService.tokenChanges.subscribe((token) => {
      if (!token && this.activeModalRef) {
        this.activeModalRef.close(); // Close the modal
        this.activeModalRef = null;
      }
    });
  }

  ngOnInit(): void {
    this.getAllClients();
  }
  private getAllClients() {
    this.clientService.findAllClient({
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
          this.clientService.updateClientIsActive(
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
          this.clientService.deleteClient({id:client_id}).subscribe(() => {
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

  viewClientDetails(client: ClientResponse) {
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

/*  totalDeliveryPointsByClient(client :ClientDto) :number{
    if(!client.deliveryDestinationsDto)return 0;
    return client.deliveryDestinationsDto.reduce((sum,dest)=>sum+(dest.deliveryPointsDto?.length||0),0);
  }*/
  openAddNewDPModal(client: any) {
    this.activeModalRef = this.modalService.open(NewDeliveryPointModalComponent, {
      size: 'xl',
      centered: true,
      backdrop: 'static'
    });

    this.activeModalRef.componentInstance.clientId = client.id;

    this.activeModalRef.result.then((result :any) => {
      if (result) {
        // Handle the new delivery point data
        console.log('New delivery point:', result);
        this.getAllClients();
        // Add your API call here
      }
    }).catch((error :any) => {
      console.log('Modal dismissed');
    });
  }

  goToClientDetails(id: number | undefined) {
    this.router.navigate(['clients','view-client',id]);
  }

  openNewDeliveyPoint(idClient: number) {
    this.router.navigate(['clients','new-delivery-points',idClient]);
  }
}
