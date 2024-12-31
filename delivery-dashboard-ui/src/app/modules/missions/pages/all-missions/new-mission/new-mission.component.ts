import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {find} from "rxjs";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MissionDto} from "../../../../../../ds-api/src/models/mission-dto";
import {EtapeDto} from "../../../../../../ds-api/src/models/etape-dto";
import {VehiculeDto} from "../../../../../../ds-api/src/models/vehicule-dto";
import {ChauffeurDto} from "../../../../../../ds-api/src/models/chauffeur-dto";
import {ChauffeurVehicleDto} from "../../../../../../ds-api/src/models/chauffeur-vehicle-dto";
import {MissionVehicleDto} from "../../../../../../ds-api/src/models/mission-vehicle-dto";
import {MissionService} from "../../../../../../ds-api/src/services/mission.service";
import {AlertService} from "../../../../../services/alert/alert.service";
import {VehiculeService} from "../../../../../../ds-api/src/services/vehicule.service";
import {ChauffeurService} from "../../../../../../ds-api/src/services/chauffeur.service";
import {ServiceDeliveryDto} from "../../../../../../ds-api/src/models/service-delivery-dto";
import {ServiceDeliveryService} from "../../../../../../ds-api/src/services/service-delivery.service";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";
import {DeliveryPointsService} from "../../../../../../ds-api/src/services/delivery-points.service";
import {et} from "@fullcalendar/core/internal-common";
import {LocalTime} from "../../../../../../ds-api/src/models/local-time";
import {CreatedByUserDto} from "../../../../../../ds-api/src/models/created-by-user-dto";
import {ActivatedRoute, Router} from "@angular/router";
import {DeliveryDestinationDto} from "../../../../../../ds-api/src/models/delivery-destination-dto";
import {DeliveryDestinationResponse} from "../../../../../../ds-api/src/models/delivery-destination-response";
import {DeliveryDestinationService} from "../../../../../../ds-api/src/services/delivery-destination.service";

import flatpickr from "flatpickr";
import {addHours, endOfWeek, format, isAfter, isBefore, isToday, parseISO} from "date-fns";

@Component({
  selector: 'app-new-mission',
  templateUrl: './new-mission.component.html',
  styleUrls: ['./new-mission.component.scss']
})
export class NewMissionComponent implements OnInit{

    /*mission related data*/

  missionToSave: MissionDto={missionVehiclesDtos:[]}; // why init like htis.??
  etapeDtos: EtapeDto[] = [];//why any?
  selectedVehicle: VehiculeDto | null=null; // why not ={}
  selectedDrivers: ChauffeurVehicleDto[] = [];
  selectedDeliveryDestination!: DeliveryDestinationDto | undefined ;//why must put on undefined
  availableVehicles: Array<VehiculeDto> = [];
  availableDrivers: Array<ChauffeurDto> = [];
  allDeliveryDestination: DeliveryDestinationResponse []=[];
  deliveryPointsByCity: DeliveryPointDto[]=[];// why must put to any
  isFormEtapeLoading:boolean=false;

  etapeDto: EtapeDto = {};
  selectedMissionVehicle: MissionVehicleDto = {};

  // Arrays to store selected vehicles and associated drivers
  selectedVehicles: {vehicleDto:VehiculeDto,chauffeursVehicleDto: ChauffeurVehicleDto[]}[]=[];
  selectedMissionVehicles: MissionVehicleDto[] = [];

  /*form group */
  missionForm:FormGroup = new FormGroup({});
  etapeForm!: FormGroup;
  //
  dateDay!: string;
  minDate: string = new Date().toISOString().split('T')[0];
  private pickUpLocation: string="";
  selectedDpoint: DeliveryPointDto={}/*deliveryPointDto={}*/;

  constructor(
    private router:Router,
    private activeRouter:ActivatedRoute,
    private fb:FormBuilder,
    private deliveryDestinationService:DeliveryDestinationService,
    private missionService:MissionService,
    private dpService:DeliveryPointsService,
    private alertService: AlertService,
    private vehicleService: VehiculeService,
    private driversService: ChauffeurService
  ) {}
  ngOnInit(): void {
    this.initForms();
    this.loadInitialData();
    this.getDateDay();
  }

  initForms(){
    this.etapeForm = this.fb.group({
      lieuDepart: new FormControl('', Validators.required),
      lieuArrivee: new FormControl('', Validators.required),
      scheduledDeliveryTime: new FormControl('', Validators.required)
    });
    this.missionForm = this.fb.group({
      titre: new FormControl('', Validators.required),
      dateDebut: new FormControl('', Validators.required),
      dateFin: new FormControl('', Validators.required),
      volume: new FormControl('', [Validators.required, Validators.min(1)])
    });

  }
  loadInitialData(){
    this.loadVehicles();
    this.loadDrivers();
    this.loadDestinationsDelivery();
  }
  private handleErrors(type:string,error:any){
    console.error(`Failed to load ${type}`,error);
    this.alertService.alertError(`Error loading ${type}. Please refresh and try again.`);

  }
  private loadVehicles() {
    this.vehicleService.findAll1().subscribe({
      next: (res) => {
        this.availableVehicles = res;
      },
      error: (err) => {
        this.handleErrors("vehicles",err)
      }
    });
  }
  private loadDrivers() {
    this.driversService.findAll17().subscribe({
      next: (drivers) => {
        this.availableDrivers = drivers;
      },
      error: (err) => {
        this.handleErrors("drivers",err)
      }
    });
  }
  private loadDestinationsDelivery() {
    this.deliveryDestinationService.findAll12().subscribe({
      next:(dDestinationResponse)=>{
        this.allDeliveryDestination=dDestinationResponse;
      },error:(err)=>{
        this.handleErrors("Destinations delivery",err)
      }
    })
  }

  onDDestinationChange(selectedDeliveryDestination: DeliveryDestinationResponse) {
    if(selectedDeliveryDestination){
      this.selectedDeliveryDestination = selectedDeliveryDestination;
      this.missionToSave.titre= "Mission - "+ selectedDeliveryDestination.serviceDelivery?.destinationCity;
      this.missionToSave.idDeliveryDestination=selectedDeliveryDestination.id;
      this.etapeForm.get('lieuDepart')?.setValue(selectedDeliveryDestination.serviceDelivery?.pickUpLocation);
      this.etapeDto.lieuDepart = this.etapeForm.value.lieuDepart;
      this.etapeForm.get('lieuArrivee')?.reset();
      this.findAllDeliveryPointsByCity(selectedDeliveryDestination.serviceDelivery?.destinationCity!);
    }else{
      this.etapeForm.get('lieuDepart')?.setValue('');
    }
  }

  addEtape(): EtapeDto {
    console.log(this.selectedDpoint.name);
    const newDeparture = this.etapeForm.value.lieuDepart;
    const newArrival = this.selectedDpoint; //this.etapeForm.value.lieuArrivee;

    const newTimeString = this.etapeForm.value.scheduledDeliveryTime;

    if (!newTimeString) {
      this.alertService.alertError("Date departure is required.");
      return {};
    }

    const isDuplicate=this.etapeDtos.some(etape=>
      etape.lieuDepart === newDeparture &&
      etape.lieuArrivee === newArrival &&
      etape.scheduledDeliveryTime === newTimeString);

    if(isDuplicate){
      this.alertService.alertWaring("This departure, destination, and time combination already exists.");
      return {};
    }

    const newScheduledDeliveryTimeParts = newTimeString.split(':').map(Number); // ["12", "44"] -> [12, 44]
    const newScheduledDeliveryTime = new Date();
    newScheduledDeliveryTime.setHours(newScheduledDeliveryTimeParts[0], newScheduledDeliveryTimeParts[1], 0, 0);

    const hasInvalidTimeDifference = this.etapeDtos.some(etape => {
      const [existingHours, existingMinutes] = (etape.scheduledDeliveryTime as string).split(':').map(Number);
      const existingScheduledDeliveryTime = new Date();
      existingScheduledDeliveryTime.setHours(existingHours, existingMinutes, 0, 0);

      return Math.abs(newScheduledDeliveryTime.getTime() - existingScheduledDeliveryTime.getTime()) < 30 * 60 * 1000;
    });

    if(hasInvalidTimeDifference){
      this.alertService.alertWaring("The time difference between stages must be at least 30 minutes.")
      return {};
    }



    if(newDeparture && newArrival && newTimeString){
      this.etapeDto = this.etapeForm.value;
      this.etapeDto.lieuArrivee=this.selectedDpoint.name;
      this.etapeDtos.push(this.etapeDto);
      //this.stageForm.reset();
      return this.etapeDto;
    } else{
      this.alertService.alertError("All filler are mandatory")
      return {};
    }
  }
  deleteEtape(i: number) {
    this.etapeDtos.splice(i, 1);
  }

  addVehicle() {
    if(this.selectedVehicle && this.selectedDrivers.length<=2 && this.selectedDrivers.length>0){

      this.selectedVehicles.push({
        vehicleDto: this.selectedVehicle,
        chauffeursVehicleDto:this.selectedDrivers.map(driver => ({
          chauffeurDto: driver
        }))
      });
      this.selectedMissionVehicle.vehicleDto=this.selectedVehicle;
      this.selectedMissionVehicle.chauffeursVehicleDto=this.selectedDrivers.map(d=>({
        chauffeurDto: d
      }));
      console.log(this.selectedMissionVehicle);

      this.selectedMissionVehicles.push(this.selectedMissionVehicle);
      this.selectedVehicle = {} as VehiculeDto;
      this.selectedMissionVehicle={} as MissionVehicleDto;
      this.selectedDrivers=[];
    }else{
      this.alertService.alertError("You must select vehicle(s) and driver(s)")
    }
  }
  removeVehicle(index:number){
    this.selectedVehicles.splice(index,1);
    this.selectedMissionVehicles.splice(index,1);
  }
  getDateDay(){
    this.dateDay= this.activeRouter.snapshot.params['dateDay'];
    if(this.dateDay){
      const parseDate = parseISO(this.dateDay);
      this.dateDay =format(parseDate,'yyyy-MM-dd HH:mm');
      this.missionToSave.dateDebut = this.dateDay;
    }
    }

  createMission(): MissionDto{
    this.missionToSave.etatMission="EN_COURS";
    this.missionToSave.etapeDtos=this.etapeDtos;
    this.missionToSave.missionVehiclesDtos=this.selectedMissionVehicles;
    return this.missionToSave;
  }

  saveMission() {
    console.log(this.createMission());
    this.missionService.save6({
      body:this.missionToSave
    }).subscribe({next:()=>{
        this.alertService.alertSuccess("Saved OK!!");
        this.router.navigate(['missions','w-planning']);
      }, error:(err)=>{
        this.alertService.alertWaring(err.error.httpCode+ " ERROR :: "+ err.error.msg)
      }});
  }

  // Helper methods
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  private findAllDeliveryPointsByCity(selectedCity :string){
    if(this.selectedDeliveryDestination){

    this.dpService.findAllByCity({city:selectedCity}).subscribe({
      next:(rep)=>{
        this.deliveryPointsByCity=rep;
      },
      error:(err)=>{
        console.log(err);
      }
    })
    }
  }

  onDeliveryPointChange(selectedDpoint: DeliveryPointDto) {
    console.log(selectedDpoint.schedule);
    this.etapeForm.get('scheduledDeliveryTime')?.setValue(selectedDpoint.schedule);
  }


  validateStartDate() {
    if(!this.missionToSave.dateDebut){
      return;
    }
    const selectedDate= new Date(this.missionToSave.dateDebut);
    const dateDay= new Date(this.dateDay);
    if(!isBefore(selectedDate.getDate(),dateDay.getDate()) && !isAfter(selectedDate.getDate(),dateDay.getDate())){
      console.log("valid date selected : ", selectedDate);
    }else{
      console.log("Invalid date selected , it must be today");
      alert(`the satrt Date must be equal to  date day ${dateDay}` );
    }
  }

  validateEndDate() {
    if(!this.missionToSave.dateFin){
      return;
    }
    const selectedEndDate= new Date(this.missionToSave.dateFin);
    const miminumDate = addHours(new Date(),7);
    const endOfCurrentWeek= endOfWeek(new Date(),{weekStartsOn:1});
    if(isAfter(selectedEndDate, miminumDate)&& isBefore(selectedEndDate,endOfCurrentWeek)){
      console.log("Valide Date");

    }else{
      console.log('Invalid end date.');
      alert('The end date must be at least 7 hours from now and less than the end of this week.');

    }
  }
}

