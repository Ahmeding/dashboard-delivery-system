import {Component, OnInit} from '@angular/core';
import {ServiceDeliveryService} from "../../../../../ds-api/src/services/service-delivery.service";
import {Router} from "@angular/router";
import {PageResponseUtilisateurDto} from "../../../../../ds-api/src/models/page-response-utilisateur-dto";
import {PageResponseServiceDeliveryDto} from "../../../../../ds-api/src/models/page-response-service-delivery-dto";
import {ServiceDeliveryDto} from "../../../../../ds-api/src/models/service-delivery-dto";
import {AlertService} from "../../../../services/alert/alert.service";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";

@Component({
  selector: 'app-services-deleivery',
  templateUrl: './services-deleivery.component.html',
  styleUrls: ['./services-deleivery.component.scss']
})
export class ServicesDeleiveryComponent implements OnInit{

  allServiceDeliveryResponse: PageResponseServiceDeliveryDto={};
  page: number=0;
  size:number=5;
  selectedServdelivery: ServiceDeliveryDto={};
  searchTerm: any;



  constructor(
    private confirmBoxService:ConfirmBoxEvokeService,
    private serviceDeliveryService:ServiceDeliveryService,
    private router:Router,
    private alertService:AlertService) {}


  navigateToNewServiceDelivery() {
      this.router.navigate(['settings','new-service-deliv']);
  }

  ngOnInit(): void {
  this.getAllServiceDel();
  }

  private getAllServiceDel() {
    this.serviceDeliveryService.findAllServicesDelivery({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(servDeliveryRep)=>{
        this.allServiceDeliveryResponse=servDeliveryRep;
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
    return this.page == this.allServiceDeliveryResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allServiceDeliveryResponse.totalPages as number -1;
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
    this.serviceDeliveryService.delete2({id:id_service}).subscribe({
      next:(resp)=>{
        this.alertService.alertInfo("deleting successfull")
        this.getAllServiceDel();
      },error:()=>{
        this.alertService.alertError("deleting failed")
      }
    })
  }
}
