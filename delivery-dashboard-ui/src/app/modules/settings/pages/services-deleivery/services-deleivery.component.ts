import {Component, OnInit} from '@angular/core';
import {ServiceDeliveryService} from "../../../../../ds-api/src/services/service-delivery.service";
import {Router} from "@angular/router";
import {PageResponseUtilisateurDto} from "../../../../../ds-api/src/models/page-response-utilisateur-dto";
import {PageResponseServiceDeliveryDto} from "../../../../../ds-api/src/models/page-response-service-delivery-dto";
import {ServiceDeliveryDto} from "../../../../../ds-api/src/models/service-delivery-dto";

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



  constructor(
    private serviceDeliveryService:ServiceDeliveryService,
    private router:Router) {}
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

  deleteServdelivery(number: number) {

  }

  editServdelivery(number: number) {

  }
}
