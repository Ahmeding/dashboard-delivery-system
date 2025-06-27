import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AlertService} from "../../../../../services/alert/alert.service";
import {DeliveryServiceRequest} from "../../../../../../ds-api/src/models/delivery-service-request";
import {DeliveryPointRequest} from "../../../../../../ds-api/src/models/delivery-point-request";
import {DeliveryPointResponse} from "../../../../../../ds-api/src/models/delivery-point-response";
import {DeliveryServiceService} from "../../../../../../ds-api/src/services/delivery-service.service";

@Component({
  selector: 'app-new-service-deliv',
  templateUrl: './new-service-deliv.component.html',
  styleUrls: ['./new-service-deliv.component.scss']
})
export class NewServiceDelivComponent implements OnInit{

  service!: DeliveryServiceRequest;
  serviceDeliveryForm!: FormGroup;
  deliveryPoints: DeliveryPointResponse[] = [];
  isEditing = false;

  constructor(
    private alertService:AlertService,
    private router:Router,
    private fb: FormBuilder,
    private serviceDlvService:DeliveryServiceService) {
  }
  ngOnInit(): void {
    this.initForm();
    // Load delivery points
    this.loadDeliveryPoints();
  }

  loadDeliveryPoints(): void {
    // Implement API call to load delivery points
    this.deliveryPoints = [
     /* { id: 1, name: 'SHOP',  deliveryPointType: 'SHOP' },
      { id: 2, name: 'DEPOT',  deliveryPointType: 'DEPOT' }*/
    ];
  }


  initForm(){
    this.serviceDeliveryForm = this.fb.group({
      id: [null],
      description: [''],
      unitPriceHT: ['', [Validators.required, Validators.min(0)]],
      tva: [20, [Validators.required, Validators.min(0), Validators.max(100)]],
      basicTripCost: [{disabled: true }],
      color: ['#ffffff',Validators.required],
      pickUpLocation: ['', Validators.required],
      distance: ['', [Validators.required, Validators.min(0)]],
      destinationCountry: ['', Validators.required],
      destinationCity: ['', Validators.required],

    });

    // Calculate TTC price when HT price or TVA changes
    this.serviceDeliveryForm.get('unitPriceHT')?.valueChanges.subscribe(() => {
      this.calculateTTC();
    });
    this.serviceDeliveryForm.get('tva')?.valueChanges.subscribe(() => {
      this.calculateTTC();
    });
  }


  saveNewDeliveryService(formValue: DeliveryServiceRequest){
    this.serviceDlvService.saveDeliveryService({body:formValue}).subscribe({
      next:(response)=>{
        console.log(response);
        this.router.navigate(['settings','services']);
        this.alertService.alertSuccess("The new service has been successfully added.");
        },
      error:(err)=>{
        console.log(err);
        this.alertService.alertError("Failed to add the new service. Please try again.");
      }
    })
  }


    calculateTTC(): void {
      const priceHT = this.serviceDeliveryForm.get('unitPriceHT')?.value || 0;
      const tva = this.serviceDeliveryForm.get('tva')?.value || 0;
      const basicTripCost = priceHT * (1 + tva / 100);
      this.serviceDeliveryForm.patchValue({ basicTripCost }, { emitEvent: false });
    }
  onSubmit(): void {
    if (this.serviceDeliveryForm.valid) {
        this.service = this.serviceDeliveryForm.getRawValue();
      console.log(this.service);
      const formValue =this.service;
      console.log('Form submitted:', formValue);
      // Implement save logic
      this.saveNewDeliveryService(formValue);
    }
  }

  reset(): void {
    this.serviceDeliveryForm.reset({
      tva: 20
    });
  }
}
