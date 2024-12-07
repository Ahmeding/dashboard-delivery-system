import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Client} from "stompjs";
import {ClientDto} from "../../../../../../ds-api/src/models/client-dto";
import {ClientService} from "../../../../../../ds-api/src/services/client.service";
import {Chart} from "chart.js";
import {DateUtilsService} from "../../../../../services/date-utils/date-utils.service";
import {NewDeliveryPointModalComponent} from "../new-delivery-point-modal/new-delivery-point-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";
import {LocalTime} from "../../../../../../ds-api/src/models/local-time";
import {DeliveryPointsService} from "../../../../../../ds-api/src/services/delivery-points.service";
import {AlertService} from "../../../../../services/alert/alert.service";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";

@Component({
  selector: 'app-view-client',
  templateUrl: './view-client.component.html',
  styleUrls: ['./view-client.component.scss']
})
export class ViewClientComponent implements OnInit{

  id_client!:number;
  client: ClientDto={};
  dpoint:DeliveryPointDto={};
  constructor(
    private modalService: NgbModal,
    private dateUtils:DateUtilsService,
    private routeActive:ActivatedRoute,
              private serviceClient:ClientService,
              private deliveryPointService:DeliveryPointsService,
    private alertService: AlertService,
    private confirmBoxService:ConfirmBoxEvokeService
  ) {
  }

  formatDate(dateString :string):string{
    return this.dateUtils.formatToDate(dateString);
  }
  revenueChart: any;

  ngOnInit(): void {
    this.id_client= this.routeActive.snapshot.params['id'];
    this.getClient();

    this.initRevenueChart();
  }

  get fullname():string{
    return this.client.firstname+" "+this.client.lastname;
  }
  private getClient() {
    this.serviceClient.findById14({id:this.id_client}).subscribe({
      next:(clientResp)=>{
        this.client=clientResp;
      },error:(err)=>{
        console.log(err);
      }
    })
  }
  initRevenueChart() {
    const ctx = document.getElementById('revenueChart') as HTMLCanvasElement;
    this.revenueChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['January', 'February', 'March','April', 'May', 'June','July', 'August', 'September','October','November'],
        datasets: [{
          data: [0, 2, 6,6, 4, 8,10, 12, 11,11, 9],
          backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc']
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }
  openAddNewDPModal(client: ClientDto) {
    const modalRef = this.modalService.open(NewDeliveryPointModalComponent, {
      size: 'lg',
      centered: true,
      backdrop: 'static'
    });

    modalRef.componentInstance.clientId = client.id;

    modalRef.result.then((result) => {
      if (result) {
        // Handle the new delivery point data
        this.getClient();
        console.log('New delivery point:', result);
        // Add your API call here
      }
    }).catch((error) => {
      console.log('Modal dismissed');
    });
  }

  deleteDeliveryPoint(id_dpoint: number) {
    if (id_dpoint === undefined) {
      this.alertService.alertError('Delivery Point ID is missing. Unable to delete Delivery Point.');
      return;
    }
    this.confirmBoxService.customFour('Are you sure you want to delete this Delivery Point?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.confirmDeleteDeliveryPoint(id_dpoint);
        }
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : 'Unable to delete Delivery Point');
      }
    )
  }
  confirmDeleteDeliveryPoint(id_dpoint: number){
    this.deliveryPointService.delete10({id:id_dpoint}).subscribe(() => {
      this.getClient();
      this.alertService.alertSuccess('Deletion successfully completed');
    });
}

  goToEditDelivery(dpoint_id: number | undefined, client: ClientDto) {
    const modalRef = this.modalService.open(NewDeliveryPointModalComponent, {
      size: 'lg',
      centered: true,
      backdrop: 'static'
    });

    modalRef.componentInstance.clientId = client.id;
    modalRef.componentInstance.dpointId = dpoint_id;

    modalRef.result.then((result) => {
      if (result) {
        // Handle the new delivery point data
        this.getClient();
        console.log('New delivery point:', result);
        // Add your API call here
      }
    }).catch((error) => {
      console.log('Modal dismissed');
    });
  }

  getStatusClass(type: "SHOP" | "DEPOT" | undefined): string {
    return type === 'SHOP' ? 'status-shop' : 'status-depot';
  }

  getTypeIcon(type: "SHOP" | "DEPOT" | undefined): string {
    return type === 'SHOP' ? 'bi bi-shop' : 'bi bi-building';
  }
}
