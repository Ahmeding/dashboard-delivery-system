import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {ServiceDeliveryDto} from "../../../../../../ds-api/src/models/service-delivery-dto";
import {Router} from "@angular/router";
import {ServiceDeliveryService} from "../../../../../../ds-api/src/services/service-delivery.service";
import {AlertService} from "../../../../../services/alert/alert.service";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";

@Component({
  selector: 'app-new-service-deliv',
  templateUrl: './new-service-deliv.component.html',
  styleUrls: ['./new-service-deliv.component.scss']
})
export class NewServiceDelivComponent implements OnInit{

  service: ServiceDeliveryDto={};
  serviceDeliveryForm!: FormGroup;
  deliveryPoints: DeliveryPointDto[] = [];
  isEditing = false;

  ngOnInit(): void {
    this.initForm();
    // Load delivery points
    this.loadDeliveryPoints();
  }

  loadDeliveryPoints(): void {
    // Implement API call to load delivery points
    this.deliveryPoints = [
      { id: 1, name: 'Point 1', city: 'Paris', deliveryPointType: 'SHOP' },
      { id: 2, name: 'Point 2', city: 'Lyon', deliveryPointType: 'DEPOT' }
    ];
  }


  initForm(){
    this.serviceDeliveryForm = this.fb.group({
      id: [null],
      reference: ['', Validators.required],
      title: ['', Validators.required],
      description: ['', Validators.required],
      unitPriceHT: ['', [Validators.required, Validators.min(0)]],
      tva: [20, [Validators.required, Validators.min(0), Validators.max(100)]],
      priceTTC: [{ value: '', disabled: true }]
    });

    // Calculate TTC price when HT price or TVA changes
    this.serviceDeliveryForm.get('unitPriceHT')?.valueChanges.subscribe(() => {
      this.calculateTTC();
    });
    this.serviceDeliveryForm.get('tva')?.valueChanges.subscribe(() => {
      this.calculateTTC();
    });
  }
  constructor(
    private alertService:AlertService,
    private router:Router,
    private fb: FormBuilder,
    private serviceDlvService:ServiceDeliveryService) {
  }


  saveNewDeliveryService(formValue: ServiceDeliveryDto){
    this.serviceDlvService.save2({body:formValue}).subscribe({
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
      const priceTTC = priceHT * (1 + tva / 100);
      this.serviceDeliveryForm.patchValue({ priceTTC }, { emitEvent: false });
    }
  onSubmit(): void {
    if (this.serviceDeliveryForm.valid) {
        this.service = this.serviceDeliveryForm.getRawValue();
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
