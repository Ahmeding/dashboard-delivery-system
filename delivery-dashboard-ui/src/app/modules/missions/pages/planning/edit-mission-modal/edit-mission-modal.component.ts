import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {AlertService} from "../../../../../services/alert/alert.service";
import {MissionService} from "../../../../../../ds-api/src/services/mission.service";
import {MissionResponse} from "../../../../../../ds-api/src/models/mission-response";
import {VehicleResponse} from "../../../../../../ds-api/src/models/vehicle-response";
import {DriverResponse} from "../../../../../../ds-api/src/models/driver-response";
import {MissionVehicleRequest} from "../../../../../../ds-api/src/models/mission-vehicle-request";
import {StopPointRequest} from "../../../../../../ds-api/src/models/stop-point-request";
import {MissionsAssignmentService} from "../../../../../../ds-api/src/services/missions-assignment.service";
import {MissionAssignmentRequest} from "../../../../../../ds-api/src/models/mission-assignment-request";
import {MissionRequest} from "../../../../../../ds-api/src/models/mission-request";
import {DriverVehicleRequest} from "../../../../../../ds-api/src/models/driver-vehicle-request";
import {MissionVehicleResponse} from "../../../../../../ds-api/src/models/mission-vehicle-response";
import {StopPointResponse} from "../../../../../../ds-api/src/models/stop-point-response";

@Component({
  selector: 'app-edit-mission-modal',
  templateUrl: './edit-mission-modal.component.html',
  styleUrls: ['./edit-mission-modal.component.scss']
})
export class EditMissionModalComponent implements OnInit {


  @Input() mission!: MissionResponse;
  @Input() vehicles!: VehicleResponse[];
  @Input() drivers!: DriverResponse[];

  tmpArrayMissionVehicles: MissionVehicleResponse[] = []; // what we want to display tmprory on html


  checkVehicleAvailability: boolean = false;
  isVehicleNotAvailable: boolean = false;
  assignedVehicleError: boolean = false;
  checkDriverAvailability: boolean = false;
  isDriverNotAvailable: boolean = false;
  assignedDriverError: boolean = false;


  editForm: FormGroup;
  currentVehicleDriversForm!: FormGroup;
  private unavailableVehicleIds!: number[];
  private unavailableDriversIds!: number[];

  constructor(
    private alertService: AlertService,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private missionService: MissionService,
    private assignmentService: MissionsAssignmentService) {

    this.editForm = this.fb.group({
      volume: [null, [Validators.required, Validators.min(1)]],
      missionVehiclesDtos: this.fb.array([])
    });
    this.createMissionVehicleForm();
  }

  get missionVehicleDtoArray(): FormArray {
    return this.editForm.get('missionVehiclesDtos') as FormArray;
  }

// 1 - init form with old data or empty missionvehicle array
  ngOnInit() {
    // Initialize form with mission data
    if (this.mission) {
      this.editForm.patchValue({volume: this.mission.volume});
      this.initMissionVehicles(); // Load vehicles/drivers
    }

  }

  createMissionVehicleForm(): FormGroup {
    return this.currentVehicleDriversForm = this.fb.group({
      id: [''],
      vehicleDto: [null, Validators.required],
      chauffeursVehicleDto: [[], Validators.required],
    })
  }

  stopPointsRequest: StopPointResponse[] = [];

  private initMissionVehicles() {
    this.createMissionVehicleForm()
    if (this.mission.missionVehicles != undefined) {
      this.tmpArrayMissionVehicles = this.mission.missionVehicles;
    }
    this.mission.stopPoints = this.stopPointsRequest;
  }

  // 2 - on select vehcile , check (duplicate or availability)

  setSelectedVehicle() {
    const v = this.currentVehicleDriversForm.get('vehicleDto')?.value as VehicleResponse;
    const startDate = this.mission.startDate;
    const endDate = this.mission.endDate;
    const currentMission: MissionVehicleRequest = {
        "driverVehicles": [
          {
            "id": 0,
            "driverId": 0,
            "missionVehicleId": 0
          }
        ],
        "missionId": 109,
        "vehicleId": 1
    /*  assignmentStatus: "ASSIGNED",
      missionStatus:"IN_PROGRESS",
      volume: 5,
      weight: 6,
      id:0,
        missionVehicles: [
          {
            vehicleId:  v.id as number,
            assignedAt: new Date().toDateString(),
            driverVehicles: [{
              driverId: 1 as number,
              missionVehicleId: 1 as number
            }],
            missionId: this.mission.id as number
          }]*/
      }
    ;
    console.log(currentMission);
    this.vehicleAvailabilityChecking(currentMission);
  }

  private missionVehicleRequest!: MissionVehicleRequest
  private vehicleAvailabilityChecking(currentMissionVehicle: MissionVehicleRequest) {
    this.missionVehicleRequest.vehicleId=0;
    this.missionVehicleRequest.driverVehicles=[];


    this.checkVehicleAvailability = true;
    this.assignmentService.checkOverlappingVehicles({
      body: this.missionVehicleRequest}).subscribe({
      next: (rep) => {
        console.log(rep);
        this.unavailableVehicleIds = [];
        this.unavailableVehicleIds = rep as unknown as number[];
        if (this.unavailableVehicleIds.length > 0) {
          this.isVehicleNotAvailable = true;
          this.assignedVehicleError = true;
        } else {
          this.isVehicleNotAvailable = false;
          this.assignedVehicleError = false;
        }
        this.checkVehicleAvailability = false;

      }, error: (err) => {
        console.log("" + err);
      }
    })
  }

  // 3 - on multi select drivers , check (duplicate or availibity)

  setSelectedDrivers() {
    const selectedDrivers: DriverResponse[] =
      this.currentVehicleDriversForm.get('chauffeursVehicleDto')?.value;

    const currentMission: MissionVehicleRequest = {
      "driverVehicles": [
      {
        "id": 0,
        "driverId": 0,
        "missionVehicleId": 0
      }
    ],
      "missionId": 109,
      "vehicleId": 1

     /* missionVehicles: [
        {
          vehicleId:  1 as number,
          assignedAt: new Date().toDateString(),
          driverVehicles: [{
            driverId: 1 as number,
            missionVehicleId: 1 as number
          }],
          missionId: this.mission.id as number
        }]*/
    }
    this.driverAvailabilityChecking(currentMission);
  }

  private driverAvailabilityChecking(currentMission: MissionVehicleRequest) {
    this.checkDriverAvailability = true;
    this.assignmentService.checkOverlappingDrivers({body: currentMission}).subscribe({
      next: (rep) => {
        console.log(rep);
        this.unavailableDriversIds = [];
        this.unavailableDriversIds = rep as number[];
        if (this.unavailableDriversIds.length > 0) {
          this.isDriverNotAvailable = true;
          this.assignedDriverError = true;
        } else {
          this.assignedDriverError = false;
          this.isDriverNotAvailable = false;
        }
        this.checkDriverAvailability = false;

      }, error: (err) => {
        console.log("" + err);
      }
    })
  }


  // 4 - put on into a tempArray (will be displayed on html)

  addMissionVehicle() {
    this.currentVehicleDriversForm.markAllAsTouched()
    if (this.currentVehicleDriversForm && this.currentVehicleDriversForm.valid) {
      const vehicleDriverFormData = this.currentVehicleDriversForm.value;
      const selectedVehicle: VehicleResponse = vehicleDriverFormData.vehicleDto;
      const selectedDrivers: DriverResponse[] = vehicleDriverFormData.chauffeursVehicleDto;
      // duplcation checking
      if (this.isVehicleDuplicate(selectedVehicle)) {
        this.alertService.alertError("This Vehicule already selected");
        return {};
      }
      if (this.isDuplicateDrivers(selectedDrivers)) {
        return {};
      }
      const tmpVehicleDriversForDisplayingToHtml: MissionVehicleResponse = {

        vehicleId: selectedVehicle.id,
        driverVehicles: selectedDrivers.map(d => ({driver: d}))
      }

      const vehiclesDriversTosave: MissionVehicleResponse = {
        vehicleId: selectedVehicle.id,
        driverVehicles: selectedDrivers.map(
          driver => ({
            driver: driver
          })
        )
      };
      // Push a new FormGroup to the missionVehiclesDtos FormArray
      this.missionVehicleDtoArray.push(
        this.fb.group({
          vehicleDto: [vehiclesDriversTosave.vehicleId, Validators.required],
          chauffeursVehicleDto: [vehiclesDriversTosave.driverVehicles, Validators.required]
        })
      )
      // Push a new vehicle / drivers to the html
      this.tmpArrayMissionVehicles.push(tmpVehicleDriversForDisplayingToHtml);
      this.currentVehicleDriversForm = this.createMissionVehicleForm();
    } else {
      this.alertService.alertError("All filler are mandatory")
      return {};
    }
    return {};
  }

  // 5 - on save
  onSave() {

    this.mission.missionVehicles = this.missionVehicleDtoArray.value;
    console.log(this.mission);
    let updatedMission!: MissionRequest;
    updatedMission.missionStatus="IN_PROGRESS";
    updatedMission.startDate=this.mission.startDate as string;
    updatedMission.title = this.mission.title as string;


    const missionToSave = this.mission;

    this.missionService.updateMissionInfo(
      {
        missionId: missionToSave.id as number,
        body: updatedMission}).subscribe({
      next: (rep) => {
        console.log(rep);
        this.activeModal.close(rep);
        this.alertService.alertSuccess("mission updated!")
      }, error: (err) => {
        console.log(err);
      }
    })


  }

  isVehicleDuplicate(selectedVehicle: VehicleResponse): boolean {
    const isDuplicate = this.tmpArrayMissionVehicles.some(mv =>
      mv.vehicleId == selectedVehicle.id)
    return isDuplicate;
  }

  removeVehicle(index: number) {
    this.missionVehicleDtoArray.removeAt(index);
    this.tmpArrayMissionVehicles.splice(index, 1);
  }

  isDuplicateDrivers(vehicleDrivers: any[]): boolean {
    for (const newDriver of vehicleDrivers) {
      const driverAlreadyAssigned = this.tmpArrayMissionVehicles.some(vehicle => {
        return vehicle.driverVehicles?.some(
          assignedDriver => assignedDriver.id === newDriver.id
        );
      });
      if (driverAlreadyAssigned) {
        this.alertService.alertWarning(
          `Driver ${newDriver.firstname || newDriver.id} is already assigned to another vehicle.`
        );
        return true;
      }
    }
    return false;
  }

  onCancel() {
    this.activeModal.dismiss()
  }

  isFieldInvalid(currentForm: FormGroup, fieldName: string) {
    const field = currentForm.get(fieldName);
    return field ?
      (field.invalid && (field.dirty || field.touched))
      : false;
  }


}
