import {Component, OnInit} from '@angular/core';
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

@Component({
  selector: 'app-new-mission',
  templateUrl: './new-mission.component.html',
  styleUrls: ['./new-mission.component.scss']
})
export class NewMissionComponent implements OnInit {

  missionToSave: MissionDto={};
  etapeToSave: EtapeDto={};
  allEtapesToSave: EtapeDto[]=[];
  availableVehicles: Array<VehiculeDto> = [];
  availableDrivers: Array<ChauffeurDto> = [];
  etapeDtos: EtapeDto[] = [];
  etapeDto: EtapeDto = {};
  etapeForm!: FormGroup;
  isFormEtapeLoading: boolean = false;
  //selectedDrivers: ChauffeurDto[] = [];
  //selectedVehicles: VehiculeDto[] = [];
  //selectedDrivers: ChauffeurDto[] = [];
  selectedDrivers: ChauffeurVehicleDto[] = [];
  selectedMissionVehicle: MissionVehicleDto = {};
  selectedDriverVehicule: ChauffeurDto = {};

  etapes: EtapeDto[] = [];

  selectedVehicles: {vehicleDto:VehiculeDto,chauffeursVehicleDto: ChauffeurVehicleDto[]}[]=[];
  selectedVehicle: VehiculeDto={};
  selectedMissionVehicles: MissionVehicleDto[] = [];
  allServiceDelivery: ServiceDeliveryDto[]=[];

  //
  deliveryPointsByCity: DeliveryPointDto[]=[];
  selectedDeliveryService!: ServiceDeliveryDto;
  private pickUpLocation: string="";
  selectedDpoint: DeliveryPointDto={}/*deliveryPointDto={}*/;


  missionForm: FormGroup = new FormGroup({});

  constructor(
    private fb:FormBuilder,
    private servDeliveryService:ServiceDeliveryService,
    private missionService:MissionService,
    private dpService:DeliveryPointsService,
    private alertService: AlertService,
    private vehicleService: VehiculeService,
    private driversService: ChauffeurService
  ) {

  }
  ngOnInit(): void {
    this.findAllServicesDelivery()
    this.findAllVehicles();
    this.findAllDrivers();
    this.initEtapeForm();
  }


  private findAllVehicles() {
    this.vehicleService.findAll().subscribe({
      next: (res) => {
        this.availableVehicles = res;
      },
      error: (err) => {

      }
    });
  }
  private findAllDrivers() {
    this.driversService.findAll15().subscribe({
      next: (res) => {
        this.availableDrivers = res;
      },
      error: (err) => {
      }
    });
  }


  initEtapeForm() {
    this.etapeForm = new FormGroup({
      lieuDepart: new FormControl(),
      lieuArrivee: new FormControl(),
      scheduledDeliveryTime: new FormControl(),

    })
  }

  addEtape(): EtapeDto {
    const newDeparture = this.etapeForm.value.lieuDepart;
    const newArrival = this.etapeForm.value.lieuArrivee;

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
        //chauffeursVehicleDto:[...this.selectedDrivers]
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
    this.selectedVehicles.splice(index,1)
  }

  createMission(): MissionDto{
    this.missionToSave.etatMission="EN_COURS";
    this.missionToSave.etapeDtos=this.etapeDtos;
    this.missionToSave.missionVehiclesDtos=this.selectedMissionVehicles;
    return this.missionToSave;
  }

  saveMission() {
    console.log(this.createMission());
    this.missionService.save5({
      body:this.missionToSave
    }).subscribe({
      next:()=>{
        this.alertService.alertSuccess("Saved OK!!")
      },
      error:(err)=>{
        this.alertService.alertWaring(err.error.httpCode+ " ERROR :: "+ err.error.msg)
      }
    });
  }


/*
  addVehicle(vehicle: any, drivers:any[]){
    if(drivers.length===0||drivers.length>2){
      this.alertService.alertWaring('Please select only 1 or 2 drivers.')
      return;
    }

    this.missionVehiclesDtos.push(
      this.fb.group({
        vehicleDto:[vehicle,Validators.required],
        chauffeursVehicleDto:this.fb.array(
          drivers.map((driver)=>{
            chauffeurDto:[driver]
          })
        ),
      })
    );
  }

  // Remove a Vehicle
  removeVehicle(index: number) {
    this.missionVehiclesDtos.removeAt(index);
  }*/



  // Helper methods
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
/*

  addVehicle() {
    if(this.selectedVehicle && this.selectedDrivers.length<=2 && this.selectedDrivers.length>0){

    this.selectedVehicles.push({
      vehicleDto: this.selectedVehicle,
      //chauffeursVehicleDto:[...this.selectedDrivers]
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
*/

/*
  removeVehicle(index:number){
    this.selectedVehicles.splice(index,1)
  }
*/

/*  saveMission() {
    console.log(this.createMission());
    this.missionService.save5({
      body:this.missionToSave
    }).subscribe({
      next:()=>{
        this.alertService.alertSuccess("Saved OK!!")
      },
      error:(err)=>{
         this.alertService.alertWaring(err.error.httpCode+ " ERROR :: "+ err.error.msg)
    }
    });
  }*/

  private findAllServicesDelivery() {
    this.servDeliveryService.findAll2().subscribe({
      next:(sdResponse)=>{
        this.allServiceDelivery=sdResponse;
      },error:(error)=>{
        console.log(error);
      }
    })
  }

  private findAllDeliveryPointsByCity(selectedCity :string){
    if(this.selectedDeliveryService){

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

  onServiceChange(selectedDeliveryService: ServiceDeliveryDto) {
    if(selectedDeliveryService && selectedDeliveryService.pickUpLocation!==null){
      this.selectedDeliveryService = selectedDeliveryService;
      this.missionToSave.titre= "Mission - "+ selectedDeliveryService.destinationCity;
      this.etapeForm.get('lieuDepart')?.setValue(selectedDeliveryService.pickUpLocation);
      this.etapeDto.lieuDepart = this.etapeForm.value.lieuDepart;
      this.findAllDeliveryPointsByCity(selectedDeliveryService.destinationCity!);
    }else{
      this.etapeForm.get('lieuDepart')?.setValue('');
    }
  }

  onDeliveryPointChange(selectedDpoint: DeliveryPointDto) {
    console.log(selectedDpoint);
    this.etapeForm.value.scheduledDeliveryTime= selectedDpoint.schedule
  }

}

