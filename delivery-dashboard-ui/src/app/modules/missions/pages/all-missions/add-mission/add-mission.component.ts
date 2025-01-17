import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {VehiculeService} from "../../../../../../ds-api/src/services/vehicule.service";
import {ChauffeurService} from "../../../../../../ds-api/src/services/chauffeur.service";
import {DeliveryDestinationService} from "../../../../../../ds-api/src/services/delivery-destination.service";
import {AlertService} from "../../../../../services/alert/alert.service";
import {DeliveryDestinationResponse} from "../../../../../../ds-api/src/models/delivery-destination-response";
import {VehiculeDto} from "../../../../../../ds-api/src/models/vehicule-dto";
import {ChauffeurDto} from "../../../../../../ds-api/src/models/chauffeur-dto";
import {MissionDto} from "../../../../../../ds-api/src/models/mission-dto";
import {CreatedByUserDto} from "../../../../../../ds-api/src/models/created-by-user-dto";
import {EtapeDto} from "../../../../../../ds-api/src/models/etape-dto";
import {MissionVehicleDto} from "../../../../../../ds-api/src/models/mission-vehicle-dto";
import {ActivatedRoute, Router} from "@angular/router";
import {addHours, differenceInDays, differenceInHours, endOfWeek, format, isAfter, isBefore, parseISO} from "date-fns";
import {DeliveryDestinationDto} from "../../../../../../ds-api/src/models/delivery-destination-dto";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";
import {DeliveryPointsService} from "../../../../../../ds-api/src/services/delivery-points.service";
import {et} from "@fullcalendar/core/internal-common";
import {ChauffeurVehicleDto} from "../../../../../../ds-api/src/models/chauffeur-vehicle-dto";

@Component({
  selector: 'app-add-mission',
  templateUrl: './add-mission.component.html',
  styleUrls: ['./add-mission.component.scss']
})
export class AddMissionComponent implements OnInit {


  dateDay!: string;
  startDate!: string
  endDate!: string

  selectedDeliveryDestination: DeliveryDestinationResponse={};
  /** populate data */
  allDeliveryDestinations: DeliveryDestinationResponse[]=[];
  allVehicles: VehiculeDto[]=[];
  allDrivers: ChauffeurDto[]=[];
  allDeliveryPointsByCity: DeliveryPointDto[]=[];
  /** **/
  minDate: string = "";
  invalidSartDate: boolean=false;
  invalidEndDate!: boolean;
  difference:{days:number;hours:number}|null=null;
  /* forms */

  missionForm: FormGroup = new FormGroup({});
  currentEtapeForm!: FormGroup;
  currentMissionVForm!: FormGroup;

  /* new list*/
  etapes: EtapeDto[]=[];
  missionVcls: MissionVehicleDto[]=[];
  vehicleDrivers:ChauffeurDto[] =[];
  /* to add to list*/
  etape:EtapeDto={};
  selectedDPoint!: DeliveryPointDto;

  missonVehicle: MissionVehicleDto={};
  selectedVehicle:VehiculeDto={};


  selectedDrivers:ChauffeurDto[]=[];

  selectedVehicles:VehiculeDto[]=[];

 constructor(
   private router:Router,
   private activeRouter:ActivatedRoute,
   private fb:FormBuilder,
   private vehiclesService:VehiculeService,
   private driversService:ChauffeurService,
   private deliveryDestinationService: DeliveryDestinationService,
   private alertService:AlertService,
   private deliveryPointService:DeliveryPointsService) {
 }
  ngOnInit() {
   this.initMissionForm();
   this.getDateDay();
   this.loadInitialData();
 }

  private initMissionForm(){
    this.missionForm = this.fb.group({
      id:[''],
      titre:['',Validators.required],
      idDeliveryDestination:[null,Validators.required],
      dateDebut:['',Validators.required],
      dateFin:['',Validators.required],
      volume:['',[Validators.required,Validators.min(1),Validators.max(100)]],
      etapeDtos:this.fb.array([]),
      missionVehiclesDtos:this.fb.array([]),
      commentaire:[],
    })
    this.createEtape();
    this.createMissionVehicle();
  }

  get missionVehiclesDtoArray():FormArray{
    return this.missionForm.get('missionVehiclesDtos') as FormArray;
  }

  createMissionVehicle(): FormGroup{
  return this.currentMissionVForm = this.fb.group({
     id:[''] ,
     vehicleDto: ['',Validators.required],
     chauffeursVehicleDto: this.fb.array([]),
   })
  }


  addMissionVehicle(){
   if(this.currentMissionVForm && this.currentMissionVForm.valid){
     this.currentMissionVForm.get('vehicleDto')?.setValue(this.selectedVehicle);
     console.log(this.currentMissionVForm.get('vehicleDto')?.value);
     this.currentMissionVForm.patchValue({
         vehicleDto: this.selectedVehicle,
         chauffeursVehicleDto: this.vehicleDrivers.map(driver=>({ chauffeurDto: driver}))
       });

   console.log(this.currentMissionVForm.get('chauffeursVehicleDto')?.value);
     this.missionVehiclesDtoArray.push(this.currentMissionVForm);
     this.missonVehicle=this.currentMissionVForm.value;
     this.missionVcls.push(this.missonVehicle);
     this.selectedVehicles.push(this.selectedVehicle);
   }
  }


  removeVehicle(index: number) {
    this.missionVehiclesDtoArray.removeAt(index);
    this.missionVcls.splice(index,1);
  }
  get etapeDtosArray():FormArray{
    return this.missionForm.get('etapeDtos') as FormArray;
  }

  createEtape(){
   this.currentEtapeForm=this.fb.group({
     id:[''],
     lieuDepart: ['', Validators.required],
     lieuArrivee: ['', Validators.required],
     scheduledDeliveryTime: ['', Validators.required]
   })

  }
  addEtape(): EtapeDto {
  if(this.currentEtapeForm && this.currentEtapeForm.valid && this.selectedDPoint.name){

    const newDepature= this.currentEtapeForm.value.lieuDepart;
    const newArrival= this.selectedDPoint.name;
    const newTimeString = this.currentEtapeForm.value.scheduledDeliveryTime;

    if(this.isEtapeDuplicate(newDepature,newArrival,newTimeString)){
      this.alertService.alertWaring('This departure, destination, and time combination already exists.');
      return {};
    }

    if(!newTimeString){
      this.alertService.alertWaring('Schedule time departure is required.');
      return{};
    }

    if(this.hasInvalidTimeDifference(newTimeString)){
      this.alertService.alertWaring("The time difference between stages must be at least 30 minutes.")
      return {};
    }

    if(newDepature&&newArrival&&newTimeString){
    this.etapeDtosArray.push(this.currentEtapeForm);
    this.etape = this.currentEtapeForm.value;
    this.etape.lieuArrivee = this.selectedDPoint.name;
    this.etape.dateDepart=this.startDate;
    this.etape.dateArrivee=this.endDate;

    this.etapes.push(this.etape);
      return this.etape;
    }else{
      this.alertService.alertError("All filler are mandatory")
      return {};
    }
  }return {};
  }

  removeEtape(index: number){
    this.etapeDtosArray.removeAt(index);
    this.etapes.splice(index,1);
  }

  private isEtapeDuplicate(newDepature: string, newArrival: string , newTimeString: string) :boolean{
    const isDuplicate = this.etapes.some(etape=>
      etape.lieuDepart === newDepature &&
      etape.lieuArrivee === newArrival &&
      etape.scheduledDeliveryTime === newTimeString);

    return isDuplicate;
  }

  private hasInvalidTimeDifference(newTimeString: string):boolean {
    const newScheduledDeliveryTimeParts = newTimeString.split(':').map(Number);
    const newScheduledDeliveryTime = new Date();
    newScheduledDeliveryTime.setHours(newScheduledDeliveryTimeParts[0], newScheduledDeliveryTimeParts[1], 0, 0);

    const hasInvalidTimeDifference = this.etapes.some(e=>{
      const [existingHours, existingMinutes] =
        (e.scheduledDeliveryTime as string).split(':').map(Number);
      const existingScheduleDeliveryTime = new Date();
      existingScheduleDeliveryTime.setHours(existingHours,existingMinutes,0,0);

      return Math.abs(newScheduledDeliveryTime.getTime() - existingScheduleDeliveryTime.getTime())<30*60*1000;
    });

    return hasInvalidTimeDifference;
  }


  private handleErrors(type:string,error:any){
    console.error(`Failed to load ${type}`,error);
    this.alertService.alertError(`Error loading ${type}. Please refresh and try again.`);
  }
  private getDateDay(){
   this.dateDay=this.activeRouter.snapshot.params['dateDay'];
   if(this.dateDay){
     const parseDateDay= parseISO(this.dateDay);
     this.dateDay=format(parseDateDay,'yyyy-MM-dd HH:mm');
     this.missionForm.get('dateDebut')?.setValue(this.dateDay);
     this.minDate = parseDateDay.toISOString().split('T')[0]
   }
 }

 private loadInitialData() {
    this.loadDeliveryDestinations();
    this.loadVehicles();
    this.loadDrivers();
    this.initDate();
  }
  private loadDeliveryDestinations(){
    this.deliveryDestinationService.findAll12().subscribe({next:(deliveryDestination)=>{
        this.allDeliveryDestinations=deliveryDestination
      },
      error:(err)=>{
        this.handleErrors("Delivery Destination",err);
      }});
  }

  private loadVehicles(){
   this.vehiclesService.findAll1().subscribe({next:(vehicles)=>{
     this.allVehicles=vehicles
     },
     error:(err)=>{
       this.handleErrors("Vehicles",err);
     }});
  }

  private loadDrivers(){
   this.driversService.findAll17().subscribe({
     next:(drivers)=>{
       this.allDrivers=drivers;
     },
     error:(err)=>{
       this.handleErrors("Drivers",err);
     }
   })
  }

  private findAllDeliveryPointsByCity(selectedCity :string){
    if(selectedCity){
      this.deliveryPointService.findAllByCity({city:selectedCity}).subscribe({
        next:(deliveryPointsByCity)=>{
          this.allDeliveryPointsByCity=deliveryPointsByCity;
        },
        error:(err)=>{
          console.log(err);
        }
      })
    }
  }

  onDDestinationChange(event: Event) {
    const selectDD = this.missionForm.get('idDeliveryDestination')?.value;
    this.selectedDeliveryDestination = selectDD as DeliveryDestinationResponse ;
    if(this.selectedDeliveryDestination) {
      const city = this.selectedDeliveryDestination.serviceDelivery?.destinationCity as string;
    this.missionForm.get("titre")?.setValue("Mission " + this.selectedDeliveryDestination.serviceDelivery?.destinationCity);
    this.currentEtapeForm.get('lieuDepart')?.setValue(this.selectedDeliveryDestination.serviceDelivery?.pickUpLocation);
    this.findAllDeliveryPointsByCity(city);
    this.currentEtapeForm.get('lieuArrivee')?.setValue(undefined);
  }else{
      this.etapeDtosArray.get('lieuDepart')?.setValue('');
    }
 }

  onDeliveryPointChange() {
    if(this.currentEtapeForm){
      const deliveryPoint = this.currentEtapeForm?.get('lieuArrivee')?.value as DeliveryPointDto;
      this.selectedDPoint=deliveryPoint;
      console.log(this.selectedDPoint);
      this.currentEtapeForm?.get('scheduledDeliveryTime')!.setValue(deliveryPoint.schedule)
    }else{
      console.warn('No current stage form selected.');
    }
  }

  onsubmit(){

  }

  isFieldInvalid(currentForm: FormGroup,fieldName: string):boolean {
   const field=currentForm.get(fieldName);
    return field ?
           (field.invalid && (field.dirty|| field.touched))
           : false;
  }

  validateStartDate() {
    if(!this.missionForm.get('dateDebut')?.value){
      return;
    }
    const selectedDate = new Date(this.missionForm.get('dateDebut')?.value);
    const dateDay=new Date(this.dateDay)
    if(isBefore(selectedDate.getDate(),dateDay.getDate()) || isAfter(selectedDate.getDate(),dateDay.getDate())){
      this.alertService.alertWaring(`the satrt Date must be equal to  date day ${dateDay}`);
      this.invalidSartDate=true;
    }else{
      this.calculateDifference(this.missionForm.get('dateDebut')?.value,
        this.missionForm.get('dateFin')?.value);

      this.startDate=this.missionForm.get('dateDebut')?.value;
      if(this.etapes){
        this.etapes.map((etape)=>{
          etape.dateDepart=this.startDate;
        })
      }
      this.invalidSartDate=false;
    }
  }

  validateEndDate() {
    if(!this.missionForm.get('dateFin')?.value){
      return;
    }
    const selectedEndDate= new Date(this.missionForm.get('dateFin')?.value);
    const miminumDate = addHours(new Date(),7);
    const endOfCurrentWeek= endOfWeek(new Date(),{weekStartsOn:1});
    if(isAfter(selectedEndDate, miminumDate)&& isBefore(selectedEndDate,endOfCurrentWeek)){
      this.calculateDifference(this.missionForm.get('dateDebut')?.value,
        this.missionForm.get('dateFin')?.value);
      console.log("Valide Date");
      this.invalidEndDate= false;
      this.endDate=this.missionForm.get('dateFin')?.value;
      if(this.etapes){
        this.etapes.map((etape)=>{
          etape.dateArrivee=this.endDate;
        })
      }
    }else{
      console.log('Invalid end date.');
      this.invalidEndDate= true;
      this.difference=null;
      this.alertService.alertWaring('The end date must be at least 7 hours from now and less than the end of this week.');

    }

  }

  calculateDifference(start:string,end:string):void{
   if(start && end){
     const starDate=new Date(start);
     const endDate=new Date(end);
     const totalHours= differenceInHours(endDate,starDate);
     const days = differenceInDays(endDate,starDate);
     const hours= totalHours - days * 24;
     this.difference ={days,hours};
   }else{
     console.log("null date");
     this.difference=null;
   }
  }


  private initDate() {
    this.startDate=this.dateDay;
    this.endDate=this.dateDay;
  }
  convertToDate(dateString: string):string{
   const dateParsed= parseISO(dateString);
   return format(dateParsed,"dd-MM-yyyy");
  }

  convertToTime(dateString: string):string{
    const dateParsed= parseISO(dateString);
    return format(dateParsed,"HH:mm");
  }

  onDriverChange($event: Event) {

    const selectElement = event?.target as HTMLSelectElement;
    const selectedOptions = Array.from(selectElement.selectedOptions).map(
      (option: HTMLOptionElement) => JSON.parse(option.value)
    );

    const chauffeursArray = this.currentMissionVForm.get('chauffeursVehicleDto') as FormArray;
   // chauffeursArray.clear(); // Clear existing drivers

    selectedOptions.forEach((driver) => chauffeursArray.push(this.fb.control(driver)))

  }
}
