import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {MissionDto} from "../../../../../../ds-api/src/models/mission-dto";
import {VehicleDto} from "../../../../../../ds-api/src/models/vehicle-dto";
import {ChauffeurDto} from "../../../../../../ds-api/src/models/chauffeur-dto";
import {MissionVehicleDto} from "../../../../../../ds-api/src/models/mission-vehicle-dto";
import {AlertService} from "../../../../../services/alert/alert.service";
import {MissionService} from "../../../../../../ds-api/src/services/mission.service";
import {EtapeDto} from "../../../../../../ds-api/src/models/etape-dto";
import {MissionResponse} from "../../../../../../ds-api/src/models/mission-response";

@Component({
  selector: 'app-edit-mission-modal',
  templateUrl: './edit-mission-modal.component.html',
  styleUrls: ['./edit-mission-modal.component.scss']
})
export class EditMissionModalComponent implements OnInit{


  @Input() mission!: MissionDto;
  @Input() vehicles!: VehicleDto[];
  @Input() drivers!: ChauffeurDto[];

  tmpArrayMissionVehicles : MissionVehicleDto[]= []; // what we want to display tmprory on html


  checkVehicleAvailability : boolean = false;
  isVehicleNotAvailable : boolean = false;
  assignedVehicleError : boolean = false;
  checkDriverAvailability : boolean = false;
  isDriverNotAvailable : boolean = false;
  assignedDriverError : boolean = false;


  editForm: FormGroup;
  currentVehicleDriversForm!:FormGroup;
  private unavailableVehicleIds!: number[];
  private unavailableDriversIds!: number[];

  constructor(
    private alertService:AlertService,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private missionService:MissionService) {

    this.editForm = this.fb.group({
      volume: [null, [Validators.required, Validators.min(1)]],
      missionVehiclesDtos: this.fb.array([])
    });
    this.createMissionVehcileForm();
  }

  get missionVehicleDtoArray():FormArray{
  return this.editForm.get('missionVehiclesDtos') as FormArray;
  }

// 1 - init form with old data or empty missionvehicle array
  ngOnInit() {
    // Initialize form with mission data
    if (this.mission) {
      this.editForm.patchValue({ volume: this.mission.volume });
      this.initMissionVehicles(); // Load vehicles/drivers
    }

  }
  createMissionVehcileForm(): FormGroup{
    return this.currentVehicleDriversForm= this.fb.group({
      id:[''],
      vehicleDto: [null,Validators.required],
      chauffeursVehicleDto: [[],Validators.required],
    })
  }
etapeDtos: EtapeDto[] = [
  {lieuDepart:"Paris - Lognes",lieuArrivee:"LV",
    dateDepart:"2025-02-26T06:00",dateArrivee:"2025-02-27T11:47"}]

  private initMissionVehicles() {
    this.createMissionVehcileForm()
    if (this.mission.missionVehiclesDtos != undefined) {
    this.tmpArrayMissionVehicles = this.mission.missionVehiclesDtos;
    }
this.mission.etapeDtos= this.etapeDtos;
  }

  // 2 - on select vehcile , check (duplicate or availability)

  setSelectedVehicle() {
    const v = this.currentVehicleDriversForm.get('vehicleDto')?.value as VehicleDto;
    const startDate = this.mission.dateDebut;
    const endDate = this.mission.dateFin;
    const currentMission : MissionDto = {
        dateDebut: startDate,
        dateFin: endDate,
        missionVehiclesDtos:[
          {vehicleDto:{id: v.id}
          }]}
      ;
    console.log(currentMission);
    this.vehicleAvailabilityChecking(currentMission);
  }
  private vehicleAvailabilityChecking(currentMission: MissionDto) {
    this.checkVehicleAvailability=true;
    this.missionService.checkOverlappingVehicles({body:currentMission}).subscribe({
      next:(rep)=>{
        console.log(rep);
        this.unavailableVehicleIds= [];
        this.unavailableVehicleIds =rep as number[];
        if(this.unavailableVehicleIds.length>0){
          this.isVehicleNotAvailable=true;
          this.assignedVehicleError = true;
        }else{
          this.isVehicleNotAvailable = false;
          this.assignedVehicleError = false;
        }
        this.checkVehicleAvailability=false;

      },error:(err)=>{
        console.log(""+err);
      }
    })
  }

  // 3 - on multi select drivers , check (duplicate or availibity)

  setSelectedDrivers() {
    const selectedDrivers: ChauffeurDto[] =
      this.currentVehicleDriversForm.get('chauffeursVehicleDto')?.value;
    const startDate = this.mission.dateDebut;
    const endDate = this.mission.dateFin ;
    const currentMission: MissionDto = {
        dateDebut: startDate,
        dateFin: endDate,
        missionVehiclesDtos:[
          {
            chauffeursVehicleDto:  selectedDrivers.map(d=>(
              {chauffeurDto: { id : d.id } }))
          }
        ]
    }
    this.driverAvailabilityChecking(currentMission);
  }
  private  driverAvailabilityChecking(currentMission:MissionDto){
    this.checkDriverAvailability=true;
    this.missionService.checkOverlappingDrivers({body:currentMission}).subscribe({
      next:(rep)=>{
        console.log(rep);
        this.unavailableDriversIds= [];
        this.unavailableDriversIds =rep as number[];
        if(this.unavailableDriversIds.length>0){
          this.isDriverNotAvailable=true;
          this.assignedDriverError = true;
        }else{
          this.assignedDriverError = false;
          this.isDriverNotAvailable=false;
        }
        this.checkDriverAvailability=false;

      },error:(err)=>{
        console.log(""+err);
      }
    })
  }


  // 4 - put on into a tempArray (will be displayed on html)

  addMissionVehicle() {
    this.currentVehicleDriversForm.markAllAsTouched()
    if(this.currentVehicleDriversForm && this.currentVehicleDriversForm.valid){
      const vehicleDriverFormData = this.currentVehicleDriversForm.value;
      const selectedVehicle: VehicleDto = vehicleDriverFormData.vehicleDto;
      const selectedDrivers: ChauffeurDto[]= vehicleDriverFormData.chauffeursVehicleDto;
      // duplcation checking
      if(this.isVehicleDuplicate(selectedVehicle)){
        this.alertService.alertError("This Vehicule already selected");
        return {};
      }if(this.isDuplicateDrivers(selectedDrivers)){
        return {};
      }
      const tmpVehicleDriversForDisplayingToHtml : MissionVehicleDto={
        vehicleDto: selectedVehicle,
        chauffeursVehicleDto:selectedDrivers.map(d=>({chauffeurDto: d}))
      }

      const vehiclesDriversTosave : MissionVehicleDto ={
        vehicleDto: {id:selectedVehicle.id},
        chauffeursVehicleDto : selectedDrivers.map(
          driver=>({
            chauffeurDto:{id: driver.id}
          })
        )
      };
      // Push a new FormGroup to the missionVehiclesDtos FormArray
      this.missionVehicleDtoArray.push(
        this.fb.group({
          vehicleDto: [vehiclesDriversTosave.vehicleDto, Validators.required],
          chauffeursVehicleDto: [vehiclesDriversTosave.chauffeursVehicleDto, Validators.required]
        })
      )
      // Push a new vehicle / drivers to the html
      this.tmpArrayMissionVehicles.push(tmpVehicleDriversForDisplayingToHtml);
      this.currentVehicleDriversForm= this.createMissionVehcileForm();
    }
    else{
      this.alertService.alertError("All filler are mandatory")
      return {};
    }
    return {};
  }

  // 5 - on save
  onSave() {
    this.mission.missionVehiclesDtos = this.missionVehicleDtoArray.value;
    console.log(this.mission);
    const missionToSave = this.mission;
    this.missionService.update({id: missionToSave.id as number,body: missionToSave}).subscribe({
      next:(rep) =>{
        console.log(rep);
        this.activeModal.dismiss();
        this.alertService.alertSuccess("mission updated!")
      },error:(err)=>{
        console.log(err);
      }
    })
  }

  isVehicleDuplicate(selectedVehicle: VehicleDto):boolean{
    const isDuplicate= this.tmpArrayMissionVehicles.some(mv=>
    mv.vehicleDto?.id == selectedVehicle.id)
    return isDuplicate;
  }

  removeVehicle(index: number) {
    this.missionVehicleDtoArray.removeAt(index);
    this.tmpArrayMissionVehicles.splice(index,1);
  }

  isDuplicateDrivers(vehicleDrivers:any[]):boolean{
  for(const newDriver of vehicleDrivers){
    const driverAlreadyAssigned = this.tmpArrayMissionVehicles.some(vehicle=>{
      return vehicle.chauffeursVehicleDto?.some(
        assignedDriver=>assignedDriver.chauffeurDto?.id === newDriver.id
      );
    });
    if(driverAlreadyAssigned){
      this.alertService.alertWaring(
        `Driver ${newDriver.firstname || newDriver.id} is already assigned to another vehicle.`
      );
      return true;
    }
  }
  return false;
}

  onCancel(){
    this.activeModal.dismiss()
  }

  isFieldInvalid(currentForm: FormGroup, fieldName: string) {
    const field=currentForm.get(fieldName);
    return field ?
      (field.invalid && (field.dirty|| field.touched))
      : false;
  }


}
