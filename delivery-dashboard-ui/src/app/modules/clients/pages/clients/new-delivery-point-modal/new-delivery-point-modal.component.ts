import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";
import {DeliveryPointsService} from "../../../../../../ds-api/src/services/delivery-points.service";
import {AdresseDto} from "../../../../../../ds-api/src/models/adresse-dto";
import {DeliveryPointContactDto} from "../../../../../../ds-api/src/models/delivery-point-contact-dto";
import {CreatedByUserDto} from "../../../../../../ds-api/src/models/created-by-user-dto";
import {ClientService} from "../../../../../../ds-api/src/services/client.service";


@Component({
  selector: 'app-new-delivery-point-modal',
  templateUrl: './new-delivery-point-modal.component.html',
  styleUrls: ['./new-delivery-point-modal.component.scss']
})
export class NewDeliveryPointModalComponent implements OnInit,AfterViewInit{


  @Input() clientId!: number;
  @Input() dpointId!: number;

  deliveryPointForm!: FormGroup;
  deliveryPoint:DeliveryPointDto={};
  selectedDeliveryPoint:DeliveryPointDto={};

  constructor(
    private clientService:ClientService,
    private deliveryPointsService:DeliveryPointsService,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder
  ){}

  ngOnInit() {

    console.log(this.dpointId);
    if(this.dpointId){
        this.getDeliveryPointById();
    }
    this.initForm();
    this.deliveryPointForm.patchValue({ clientId: this.clientId });
  }

  getDeliveryPointById(){
    this.deliveryPointsService.findById9({id:this.dpointId}).subscribe({
      next:(dpResp)=>{
        this.selectedDeliveryPoint=dpResp;

        console.log(this.selectedDeliveryPoint);

        this.deliveryPointForm.patchValue(this.selectedDeliveryPoint);
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }


  initForm(){
  this.deliveryPointForm = this.fb.group({
    id:[this.selectedDeliveryPoint.id ||'', Validators.required],
    name: [this.selectedDeliveryPoint.name ||'', Validators.required],
    deliveryPointType: [this.selectedDeliveryPoint.deliveryPointType||'', Validators.required],
    city: [this.selectedDeliveryPoint.city||'', Validators.required],
    schedule: [this.selectedDeliveryPoint.schedule||'', [Validators.required]],
    contact: this.fb.group({
      email: [this.selectedDeliveryPoint.contact?.email||'', [Validators.email]], // Add Validators.email for validation
      firstname: [this.selectedDeliveryPoint.contact?.firstname||'', Validators.required],
      lastname: [this.selectedDeliveryPoint.contact?.lastname||'', Validators.required],
      phoneNum: [this.selectedDeliveryPoint.contact?.phoneNum||'', Validators.required]
    }),
    addressDto: this.fb.group({
      adresse1: [this.selectedDeliveryPoint.addressDto?.adresse1||'', Validators.required],
      adresse2: [this.selectedDeliveryPoint.addressDto?.adresse2||''], // Optional, no Validators
      codepostal: [this.selectedDeliveryPoint.addressDto?.codepostal||'', Validators.required],
      pays: [this.selectedDeliveryPoint.addressDto?.pays||'', Validators.required],
      ville: [this.selectedDeliveryPoint.addressDto?.ville||'', Validators.required]
    }),
    clientId: ['']
  });
}


   get name() { return this.deliveryPointForm.get('name'); }
   get deliveryPointType() { return this.deliveryPointForm.get('deliveryPointType'); }
   get city() { return this.deliveryPointForm.get('city'); }
   get schedule() { return this.deliveryPointForm.get('schedule'); }
   get email() { return this.deliveryPointForm.get('contact.email'); }
   get firstname() { return this.deliveryPointForm.get('contact.firstname'); }
   get lastname(){ return this.deliveryPointForm.get('contact.lastname'); }
   get phoneNum() { return this.deliveryPointForm.get('contact.phoneNum'); }

  save() {
    if (this.deliveryPointForm.valid) {
      this.deliveryPoint=this.deliveryPointForm.value;
      this.deliveryPointsService.save9({body:this.deliveryPoint}).subscribe({
        next:(dpResp)=>{
          console.log(dpResp);

          this.activeModal.close(this.deliveryPointForm.value);
        },error:(err)=>{
          console.log(err);
        }
      })

    }
  }

  ngAfterViewInit(): void {

  }
}
