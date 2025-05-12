import {Component, Input, OnInit} from '@angular/core';
import {DeliveryDestinationDto} from "../../../../../../ds-api/src/models/delivery-destination-dto";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";
import {ServiceDeliveryDto} from "../../../../../../ds-api/src/models/service-delivery-dto";
import {DeliveryPointCategoryDto} from "../../../../../../ds-api/src/models/delivery-point-category-dto";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {InputFillUtilsService} from "../../../../../services/input-fill-utils/input-fill-utils.service";
import {DeliveryPointCategoriesService} from "../../../../../../ds-api/src/services/delivery-point-categories.service";
import {ServiceDeliveryService} from "../../../../../../ds-api/src/services/service-delivery.service";
import {DeliveryPointsService} from "../../../../../../ds-api/src/services/delivery-points.service";
import {DeliveryDestinationService} from "../../../../../../ds-api/src/services/delivery-destination.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {AlertService} from "../../../../../services/alert/alert.service";
import {ClientDto} from "../../../../../../ds-api/src/models/client-dto";
import {ActivatedRoute, Router} from "@angular/router";
import {RecurrencePatternDto} from "../../../../../../ds-api/src/models/recurrence-pattern-dto";

@Component({
  selector: 'app-new-delivery-point',
  templateUrl: './new-delivery-point.component.html',
  styleUrls: ['./new-delivery-point.component.scss']
})
export class NewDeliveryPointComponent  implements OnInit{


  clientId!: number;
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
  weekDays = [
    { label: 'Sun', value: 'SUNDAY' },
    { label: 'Mon', value: 'MONDAY' },
    { label: 'Tue', value: 'TUESDAY' },
    { label: 'Wed', value: 'WEDNESDAY' },
    { label: 'Thu', value: 'THURSDAY' },
    { label: 'Fri', value: 'FRIDAY' },
    { label: 'Sat', value: 'SATURDAY' }
  ];

  constructor(
    private routeActive: ActivatedRoute,
    private router:Router,
    private fillInputService: InputFillUtilsService,
    private dpCategoryService:DeliveryPointCategoriesService,
    private serviceDeliveryService:ServiceDeliveryService,
    private deliveryPointsService:DeliveryPointsService,
    private dDestinationService:DeliveryDestinationService,
    private fb: FormBuilder,
    private alertService: AlertService
  ){this.initDestForm();
    this.clientId= this.routeActive.snapshot.params['id'];}


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

      recurrencePatternDto: this.fb.group({
        startDate:['', Validators.required],
        endDate:['', Validators.required],
        recurrenceInterval:['', Validators.required],
        daysOfWeek:this.fb.array([], Validators.required),
      }),

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

  getDayeOfWeekArray():string [] {
    if(this.currentPointForm){
    return this.currentPointForm.get('recurrencePatternDto.daysOfWeek')?.value || [];
    }
    return [];
  }

  updateDayOfWeek(day:string, isChecked:Event){
    if(this.currentPointForm){

      const checked= (isChecked.target as HTMLInputElement).checked;
      const daysFormArray =  this.currentPointForm
        .get("recurrencePatternDto.daysOfWeek") as FormArray;
      if(checked){
        daysFormArray.push(this.fb.control(day));
      }else{
        const index =  daysFormArray.controls.findIndex(x=>x.value === day);
        daysFormArray.removeAt(index);
      }
    }
  }
  addDeliveryPoint(){
    if(this.currentPointForm && this.currentPointForm.valid){
      this.currentPointForm.get("clientId")?.setValue(this.clientId);
      this.deliveryPointsArray.push(this.currentPointForm);
      this.deliveryPoints.push(this.currentPointForm.value);
      console.log(this.currentPointForm.value);
      this.currentPointForm=null;
    }
  }

  removeDeliveryPoint(index:number){
    this.deliveryPointsArray.removeAt(index);
    this.deliveryPoints.splice(index,1)
  }

  getDeliveryPointById(){
    this.deliveryPointsService.findById12({id:this.dpointId}).subscribe({
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



  save() {

        if (this.destinationForm.valid) {
          this.destinationForm.get("serviceDeliveryId")?.setValue(this.selectedDeliveryService.id)
          const deliveryDest=this.destinationForm.getRawValue();
          console.log( );
          this.dDestinationService.save13({body:deliveryDest}).subscribe({
            next:(dpResp)=>{
              console.log(dpResp);
              this.alertService.alertSuccess("New Delivery Point added successfully!");
              //this.activeModal.close(this.destinationForm.value);
            },error:(err)=>{
              this.alertService.alertWarning("An error occurred while adding the new delivery point. ["+ err.error.msg+"]");
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
    this.dpCategoryService.findAll10().subscribe({
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

  backToClientsPage() {

      this.router.navigate(['clients','all'])

  }
}
