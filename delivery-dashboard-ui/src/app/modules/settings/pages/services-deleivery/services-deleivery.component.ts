import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AlertService} from "../../../../services/alert/alert.service";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {
  PageResponseDeliveryServiceResponse
} from "../../../../../ds-api/src/models/page-response-delivery-service-response";
import {DeliveryServiceResponse} from "../../../../../ds-api/src/models/delivery-service-response";
import {DeliveryServiceService} from "../../../../../ds-api/src/services/delivery-service.service";

@Component({
  selector: 'app-services-deleivery',
  templateUrl: './services-deleivery.component.html',
  styleUrls: ['./services-deleivery.component.scss']
})
export class ServicesDeleiveryComponent implements OnInit{

  allDeliveryServiceResponse!: PageResponseDeliveryServiceResponse;
  page: number=0;
  size:number=5;
  selectedServdelivery!: DeliveryServiceResponse;
  searchTerm: any;



  constructor(
    private confirmBoxService:ConfirmBoxEvokeService,
    private DeliveryServiceService:DeliveryServiceService,
    private router:Router,
    private alertService:AlertService) {}


  navigateToNewServiceDelivery() {
      this.router.navigate(['settings','new-service-deliv']);
  }

  ngOnInit(): void {
  this.getAllServiceDel();
  }

  private getAllServiceDel() {
    this.DeliveryServiceService.findAllDeliveryServicesPageable({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(servDeliveryRep)=>{
        this.allDeliveryServiceResponse=servDeliveryRep;
      },error:(err)=>{
      console.log(err);
    }
    })
  }

  deleteServdelivery(id_service: number) {
    if (id_service === undefined) {
      this.alertService.alertError('Service Delivery ID is missing. Unable to delete S. delivery.');
      return;
    }
    this.confirmBoxService.customFour('Are you sure you want to delete this Service Delivery?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.confirmDeliting(id_service);
        }
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : 'Unable to delete Service Delivery');
      }
    )

  }




  goToPreviousPage() {
    this.page--;
    this.getAllServiceDel();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllServiceDel();
  }

  goToNextPage() {
    this.page++;
    this.getAllServiceDel();
  }


  get  isLastPage():boolean{
    return this.page == this.allDeliveryServiceResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allDeliveryServiceResponse.totalPages as number -1;
  }

  goToFirstPage() {
    this.page=0;
    this.getAllServiceDel();
  }


  editServdelivery(number: number) {

  }

  filterServices() {

  }

  onPageSizeChange() {

  }

  private confirmDeliting(id_service: number) {
    console.log(id_service);
        this.DeliveryServiceService.deleteDeliveryService({id:id_service}).subscribe({
          next:(resp)=>{
            this.alertService.alertInfo("deleting successfull")
            this.getAllServiceDel();
          },error:()=>{
            this.alertService.alertError("deleting failed")
          }
        });
  }

  handleServiceDeliveryActivation(idServDelivery: number, isActive: boolean) {
    console.log("===========before=========== "+ isActive);
    const action = isActive? "disable" : "enable"; const updateIsActive : boolean = !(isActive);
    if (idServDelivery === undefined) {
      this.alertService.alertError('Delivery Service ID is missing. Unable to delete.');
      return;
    }
    this.confirmBoxService.customFour(`Are you sure you want to ${action} this Delivery Service?`, 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        console.log("============updateIsActive========== "+ updateIsActive);

        this.updateDeliveryServiceStatus(idServDelivery,updateIsActive);
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : `Unable to ${action} Delivery Service`);
      }
    )
  }

  private updateDeliveryServiceStatus(idServDelivery: number, updateIsActive: boolean) {
    this.DeliveryServiceService.updateDeliveryServiceEnabled({
      enable:updateIsActive,
      id:idServDelivery,
    }).subscribe({
      next:(res)=>{

        this.getAllServiceDel();
        this.alertService.alertSuccess("Delivery Service successfully deactivated");
      },
      error:(err)=>{
        this.alertService.alertWarning("Delivery Service deactivation failed");
      }
    })
  }
}
