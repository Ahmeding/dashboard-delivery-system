import {Component, OnInit} from '@angular/core';
import {VehiculeDto} from "../../../../../../../ds-api/src/models/vehicule-dto";
import {VehiculeService} from "../../../../../../../ds-api/src/services/vehicule.service";
import {find} from "rxjs";
import {ChauffeurDto} from "../../../../../../../ds-api/src/models/chauffeur-dto";
import {ChauffeurService} from "../../../../../../../ds-api/src/services/chauffeur.service";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {EtapeDto} from "../../../../../../../ds-api/src/models/etape-dto";
import {MissionVehicleDto} from "../../../../../../../ds-api/src/models/mission-vehicle-dto";
import {AlertService} from "../../../../../../services/alert/alert.service";
import {MissionDto} from "../../../../../../../ds-api/src/models/mission-dto";
import {MissionService} from "../../../../../../../ds-api/src/services/mission.service";
import {ChauffeurVehicleDto} from "../../../../../../../ds-api/src/models/chauffeur-vehicle-dto";

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
  selectedVehicles: {vehicleDto:VehiculeDto,chauffeursVehicleDto: ChauffeurVehicleDto[]}[]=[];
  selectedVehicle: VehiculeDto={};
  //selectedDrivers: ChauffeurDto[] = [];
  selectedDrivers: ChauffeurVehicleDto[] = [];
  selectedMissionVehicles: MissionVehicleDto[] = [];
  selectedMissionVehicle: MissionVehicleDto = {};
  selectedDriverVehicule: ChauffeurDto = {};

  constructor(
    private missionService:MissionService,
    private alertService: AlertService,
    private vehicleService: VehiculeService,
    private driversService: ChauffeurService
  ) {
  }
  ngOnInit(): void {
    this.findAllVehicles();
    this.findAllDrivers();
    this.initEtapeForm();
  }

  initEtapeForm() {
    this.etapeForm = new FormGroup({
      lieuDepart: new FormControl(),
      lieuArrivee: new FormControl(),
      dateDepart: new FormControl()
    })
  }

  addEtape(): EtapeDto {
    if(this.etapeForm.value.lieuDepart!== null && this.etapeForm.value.lieuArrivee
    !==null && this.etapeForm.value.dateDepart !=null){
      this.etapeDto = this.etapeForm.value;
      this.etapeDto.lieuDepart = this.etapeForm.value.lieuDepart;
      this.etapeDto.lieuArrivee = this.etapeForm.value.lieuArrivee;
      this.etapeDto.dateDepart = this.etapeForm.value.dateDepart;
      this.etapeDtos.push(this.etapeDto);
      //this.etapeDto={};
      this.etapeForm.reset();
      return this.etapeDto;
      } else{
        this.alertService.alertError("All filler are mandatory")
        return {};
      }
  }
  createEtapeToSave(){

    }
  deleteEtape(i: number) {
    this.etapeDtos.splice(i, 1);
    this.alertService.alertError("All filler are mandatory")
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
    this.driversService.findAll12().subscribe({
      next: (res) => {
        this.availableDrivers = res;
      },
      error: (err) => {
      }
    });
  }

  onsubmitmit() {
    console.log('Mission data:', this.selectedVehicles);
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
}

