  import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";
import {DeliveryPointsService} from "../../../../../../ds-api/src/services/delivery-points.service";
import {ClientService} from "../../../../../../ds-api/src/services/client.service";
import {DeliveryDestinationDto} from "../../../../../../ds-api/src/models/delivery-destination-dto";
import {ServiceDeliveryDto} from "../../../../../../ds-api/src/models/service-delivery-dto";
import {ServiceDeliveryService} from "../../../../../../ds-api/src/services/service-delivery.service";
import {DeliveryPointCategoryDto} from "../../../../../../ds-api/src/models/delivery-point-category-dto";
import {DeliveryPointCategoriesService} from "../../../../../../ds-api/src/services/delivery-point-categories.service";
import {DeliveryDestinationService} from "../../../../../../ds-api/src/services/delivery-destination.service";
import {InputFillUtilsService} from "../../../../../services/input-fill-utils/input-fill-utils.service";
  import {AlertService} from "../../../../../services/alert/alert.service";


@Component({
  selector: 'app-new-delivery-point-modal',
  templateUrl: './new-delivery-point-modal.component.html',
  styleUrls: ['./new-delivery-point-modal.component.scss']
})
export class NewDeliveryPointModalComponent implements OnInit{


  @Input() clientId!: number;
  @Input() dpointId!: number;

  deliveryDestination:DeliveryDestinationDto={};
  deliveryPoint:DeliveryPointDto={};

  selectedDeliveryPoint:DeliveryPointDto={};
  serviceDeliveries: ServiceDeliveryDto[]=[];
  deliveryPoints: DeliveryPointDto[]=[];
  dpCategories:DeliveryPointCategoryDto[]=[];
  selectedDeliveryService!:ServiceDeliveryDto;

  destinationForm!: FormGroup;
  deliveryPointForm!: FormGroup;
  currentPointForm: FormGroup | null = null;

  constructor(
    private fillInputService: InputFillUtilsService,
    private dpCategoryService:DeliveryPointCategoriesService,
    private serviceDeliveryService:ServiceDeliveryService,
    private deliveryPointsService:DeliveryPointsService,
    private dDestinationService:DeliveryDestinationService,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private alertService: AlertService
  ){this.initDestForm();}


  initDestForm(){
    this.destinationForm = this.fb.group({
      id:[''],
      serviceDeliveryId: ['', Validators.required],
      deliveryPointsDto: this.fb.array([])
    });
  }

  get deliveryPointsArray(){
    return this.destinationForm.get('deliveryPointsDto') as FormArray;
  }


  ngOnInit() {
    this.getAllCategories()
    this.getAllServiceDelivery()
    if(this.dpointId){
        this.getDeliveryPointById();
    }
    this.destinationForm.patchValue({clientId: this.clientId})
  }

  createDeliveryPoint(){
    this.currentPointForm=this.fb.group({
      id:[''],
      clientId: [''],
      name:['', Validators.required],
      deliveryPointType:['', Validators.required],
      deliveryPointCategoryId:['', Validators.required],
      schedule:['09:00', Validators.required],

      addressDto:this.fb.group({
            adresse1:['', Validators.required],
            adresse2:[''],
            codepostal:['', Validators.required],
            ville:['', Validators.required],
            pays:['', Validators.required]
      }),
      contact:this.fb.group({
        email: ['',[Validators.required,Validators.email] ],
        firstname: ['', Validators.required],
        lastname: ['', Validators.required],
        phoneNum: ['', Validators.required]
      })
    })
  }

  addDeliveryPoint(){
    if(this.currentPointForm && this.currentPointForm.valid){
      this.currentPointForm.get("clientId")?.setValue(this.clientId);
      this.deliveryPointsArray.push(this.currentPointForm);
      this.deliveryPoints.push(this.currentPointForm.value);
      this.currentPointForm=null;
    }
  }

  removeDeliveryPoint(index:number){
    this.deliveryPointsArray.removeAt(index);
    this.deliveryPoints.splice(index,1)
  }

  getDeliveryPointById(){
    this.deliveryPointsService.findById11({id:this.dpointId}).subscribe({
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


 /* initForm(){
  this.deliveryPointForm = this.fb.group({
    id:[this.selectedDeliveryPoint.id || ''],
    name: [this.selectedDeliveryPoint.name || '', Validators.required],
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
}*/

/*
   get name() { return this.deliveryPointForm.get('name'); }
   get deliveryPointType() { return this.deliveryPointForm.get('deliveryPointType'); }
   get city() { return this.deliveryPointForm.get('city'); }
   get schedule() { return this.deliveryPointForm.get('schedule'); }
   get email() { return this.deliveryPointForm.get('contact.email'); }
   get firstname() { return this.deliveryPointForm.get('contact.firstname'); }
   get lastname(){ return this.deliveryPointForm.get('contact.lastname'); }
   get phoneNum() { return this.deliveryPointForm.get('contact.phoneNum'); }

*/

  save() {
    if (this.destinationForm.valid) {
      this.destinationForm.get("serviceDeliveryId")?.setValue(this.selectedDeliveryService.id)
      const deliveryDest=this.destinationForm.getRawValue();
      console.log( );
      this.dDestinationService.save12({body:deliveryDest}).subscribe({
        next:(dpResp)=>{
          console.log(dpResp);

          this.alertService.alertSuccess("New Delivery Point added successfully!");
          this.activeModal.close(this.destinationForm.value);
        },error:(err)=>{
          this.alertService.alertWaring("An error occurred while adding the new delivery point. ["+ err.error.msg+"]");
          console.log(err.error.msg);
        }
      })
      console.log("save !!!! ");
    }
  }



  private getAllServiceDelivery() {
    this.serviceDeliveryService.findAll3().subscribe({
      next:(response)=>{
        this.serviceDeliveries=response;
      }
    })

  }

  private getAllCategories() {
    this.dpCategoryService.findAll9().subscribe({
      next:(dpCatRep)=>{
        this.dpCategories=dpCatRep;
      },error:(err)=>{
        console.log(err);

      }
    })
  }

  inputFillDataOnClick() {
    const name = this.fillInputService.generateRandomName();
    if (this.currentPointForm) {
      this.currentPointForm.get('contact.firstname')?.setValue(name.firstName)
      this.currentPointForm.get('contact.lastname')?.setValue(name.lastName)
      this.currentPointForm.get('contact.email')?.setValue(name.email)

      const numPhone = this.fillInputService.generateFrenchPhoneNumber();
      this.currentPointForm.get('contact.phoneNum')?.setValue(numPhone)

      const numNameStreet = this.fillInputService.generateRandomFrenchAddress();
      this.currentPointForm.get('addressDto.adresse1')?.setValue(numNameStreet)

      const randomCity = this.fillInputService.generateRandomCityAndPostalCode();
      if(this.selectedDeliveryService){
        this.currentPointForm.get('addressDto.codepostal')?.setValue(randomCity.postalCode)
        this.currentPointForm.get('addressDto.ville')?.setValue(this.selectedDeliveryService.destinationCity)
        this.currentPointForm.get('addressDto.pays')?.setValue(this.selectedDeliveryService.destinationCountry)
      }

    }
  }

  setAddressLocation(selectedDeliveryService: ServiceDeliveryDto) {
   this.selectedDeliveryService = this.destinationForm.get("serviceDeliveryId")?.value;
    console.log(selectedDeliveryService.destinationCity+"----------------");
  }
}
