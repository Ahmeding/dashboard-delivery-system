import { Component } from '@angular/core';
import {FormGroup, NgForm} from "@angular/forms";
import {ServiceDeliveryDto} from "../../../../../../ds-api/src/models/service-delivery-dto";
import {Router} from "@angular/router";
import {ServiceDeliveryService} from "../../../../../../ds-api/src/services/service-delivery.service";
import {AlertService} from "../../../../../services/alert/alert.service";

@Component({
  selector: 'app-new-service-deliv',
  templateUrl: './new-service-deliv.component.html',
  styleUrls: ['./new-service-deliv.component.scss']
})
export class NewServiceDelivComponent {
  service: ServiceDeliveryDto={};

  constructor(
    private alertService:AlertService,
    private router:Router,
    private serviceDlvService:ServiceDeliveryService) {
  }
  onSubmit(serviceForm: NgForm) {
      this.saveNewDeliveryService(serviceForm);
  }

  saveNewDeliveryService(serviceForm : NgForm){
    this.serviceDlvService.save2({body:this.service}).subscribe({
      next:(response)=>{
        console.log(response);
        this.resetForm(serviceForm);
        this.router.navigate(['settings','services']);
        this.alertService.alertSuccess("The new service has been successfully added.");
        },
      error:(err)=>{
        console.log(err);
        this.alertService.alertError("Failed to add the new service. Please try again.");
      }
    })
  }
  resetForm(serviceForm : NgForm){
      serviceForm.resetForm();
      this.service={};
  }
}
