import {Component, Input, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {InputFillUtilsService} from "../../../../../services/input-fill-utils/input-fill-utils.service";
import {DeliveryPointCategoriesService} from "../../../../../../ds-api/src/services/delivery-point-categories.service";
import {DeliveryPointsService} from "../../../../../../ds-api/src/services/delivery-points.service";
import {DeliveryDestinationService} from "../../../../../../ds-api/src/services/delivery-destination.service";
import {AlertService} from "../../../../../services/alert/alert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormUtilsService} from "../../../../../components/shared/services/form-utils/form-utils.service";
import {DeliveryDestinationResponse} from "../../../../../../ds-api/src/models/delivery-destination-response";
import {DeliveryPointRequest} from "../../../../../../ds-api/src/models/delivery-point-request";
import {DeliveryPointResponse} from "../../../../../../ds-api/src/models/delivery-point-response";
import {DeliveryServiceResponse} from "../../../../../../ds-api/src/models/delivery-service-response";
import {DeliveryPointCatResponse} from "../../../../../../ds-api/src/models/delivery-point-cat-response";
import {DeliveryServiceService} from "../../../../../../ds-api/src/services/delivery-service.service";

@Component({
  selector: 'app-new-delivery-point',
  templateUrl: './new-delivery-point.component.html',
  styleUrls: ['./new-delivery-point.component.scss']
})
export class NewDeliveryPointComponent  implements OnInit{


  clientId!: number;
  @Input() dpointId!: number;

  //track form submission
  formSubmitted = false;
  selectedDeliveryPoint:DeliveryPointResponse={};
  serviceDeliveries: DeliveryServiceResponse[]=[];
  deliveryPoints: DeliveryPointResponse[]=[];
  dpCategories:DeliveryPointCatResponse[]=[];
  selectedDeliveryService!:DeliveryServiceResponse;

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
    private formUtils:FormUtilsService,
    private routeActive: ActivatedRoute,
    private router:Router,
    private fillInputService: InputFillUtilsService,
    private dpCategoryService:DeliveryPointCategoriesService,
    private deliveryServiceService:DeliveryServiceService,
    private deliveryPointsService:DeliveryPointsService,
    private dDestinationService:DeliveryDestinationService,
    private fb: FormBuilder,
    private alertService: AlertService
  ){this.initDestForm();
    this.clientId= this.routeActive.snapshot.params['id'];}

isValidField(currentForm:FormGroup,fieldName:string): boolean{
    return  this.formUtils.isFieldInvalid(currentForm
    ,fieldName);
}
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
        daysOfWeek:this.fb.array([],this.atLeastOneDayValidator()),
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
  get daysArray(): FormArray {
    return this.currentPointForm?.get('recurrencePatternDto.daysOfWeek') as FormArray;
  }
  getDayeOfWeekArray():string [] {
    if(this.currentPointForm){
    return this.currentPointForm.get('recurrencePatternDto.daysOfWeek')?.value || [];
    }
    return [];
  }

  updateDayOfWeek(dayValue:string, isChecked:Event){
      const checked= (isChecked.target as HTMLInputElement).checked;
      const daysFormArrayControl =  this.daysArray;
        if(checked){
            daysFormArrayControl.push(this.fb.control(dayValue))
        }else{
          const index = daysFormArrayControl.value.indexOf(dayValue);
        daysFormArrayControl.removeAt(index);
        }
    daysFormArrayControl.updateValueAndValidity();
  }



  addDeliveryPoint(){
    this.formSubmitted = true;

    if (this.currentPointForm!.invalid) {
      // Mark all controls as touched to show errors
      this.currentPointForm!.markAllAsTouched();
      return;
    }
    if(this.currentPointForm && this.currentPointForm.valid){
      this.currentPointForm.get("clientId")?.setValue(this.clientId);
      this.deliveryPointsArray.push(this.currentPointForm);
      this.deliveryPoints.push(this.currentPointForm.value);
      console.log(this.currentPointForm.value);
      this.currentPointForm=null;
    }

    // Reset submission state
    this.formSubmitted = false;
  }

  private atLeastOneDayValidator():ValidatorFn{
   return (control: AbstractControl) : ValidationErrors | null =>{
     if(!control.value || control.value.length === 0){
       return { required: true};
     }
    return null;
   };
  }
  removeDeliveryPoint(index:number){
    this.deliveryPointsArray.removeAt(index);
    this.deliveryPoints.splice(index,1)
  }

  getDeliveryPointById(){
    this.deliveryPointsService.findDeliveryPointById({id:this.dpointId}).subscribe({
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
          this.dDestinationService.saveDeliveryDestination({body:deliveryDest}).subscribe({
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
    this.deliveryServiceService.findAllDeliveryService().subscribe({
      next:(response)=>{
        this.serviceDeliveries=response;
      }
    })

  }

  private getAllCategories() {
    this.dpCategoryService.findAllDeliveryPointCategory().subscribe({
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

  setAddressLocation(selectedDeliveryService: DeliveryServiceResponse) {
    this.selectedDeliveryService = this.destinationForm.get("serviceDeliveryId")?.value;
    console.log(selectedDeliveryService.destinationCity+"----------------");
  }

  backToClientsPage() {

      this.router.navigate(['clients','all'])

  }

  isEmptyDaysOfWeek(): boolean {
    //const daysOfWeek = this.currentPointForm.get('recurrence')
    const daysArray = this.getDayeOfWeekArray();
    return daysArray.length === 0;
  }
}
