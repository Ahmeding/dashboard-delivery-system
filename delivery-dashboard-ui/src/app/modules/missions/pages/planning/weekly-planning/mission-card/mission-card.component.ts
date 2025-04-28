import {Component, Input, Output, EventEmitter, ViewChild, OnInit} from '@angular/core';
import { MissionCard } from '../models/mission.model';
import {MissionDto} from "../../../../../../../ds-api/src/models/mission-dto";
import {MissingFileReplacementException} from "@angular-devkit/build-angular/src/utils";
import {MissionResponse} from "../../../../../../../ds-api/src/models/mission-response";
import {formatTime} from "../utils/planning.utils";
import {format} from "date-fns";
import {DateUtilsService} from "../../../../../../services/date-utils/date-utils.service";
import {ChauffeurDto} from "../../../../../../../ds-api/src/models/chauffeur-dto";
import {VehiculeService} from "../../../../../../../ds-api/src/services/vehicule.service";
import {ChauffeurService} from "../../../../../../../ds-api/src/services/chauffeur.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MissionService} from "../../../../../../../ds-api/src/services/mission.service";
import {convertResponseToDto} from "../../../../../../services/dto-converters/mission-converter";
import {VehicleDto} from "../../../../../../../ds-api/src/models/vehicle-dto";
import {EditMissionModalComponent} from "../../edit-mission-modal/edit-mission-modal.component";

@Component({
  selector: 'app-mission-card',
  templateUrl: './mission-card.component.html',
  styleUrls: ['./mission-card.component.scss']
})
export class MissionCardComponent implements OnInit{


  @Input() mission!: MissionResponse/*MissionCard*/;
  @Output() edit = new EventEmitter<number>();
  @Output() delete = new EventEmitter<number>();
  @ViewChild(EditMissionModalComponent) editModal!: EditMissionModalComponent;

  // Load these from your service
  availableVehicles: VehicleDto[] = [];
  availableDrivers: ChauffeurDto[] = [];


  constructor(
    private missionService: MissionService,
    private modalService: NgbModal,
    private dateService:DateUtilsService,
              private vehicleService:VehiculeService,
              private driverService:ChauffeurService){}


  private loadAvailableVehicles(){
    this.vehicleService.findAll1().subscribe(
      {next: (allV) =>{
        this.availableVehicles =allV;
        },
      error:(err)=>{
        console.log(err);
      }
      }
    );
  }
  private loadAvailableDrivers(){
this.driverService.findAll18().subscribe({
  next:(allD)=>{
    this.availableDrivers=allD;
  },
  error:(err)=>{
    console.log(err);
  }
})
  }
  private loadData(){
this.loadAvailableVehicles();
this.loadAvailableDrivers();
  }

  formattedTime: string = '';
  formatTime =formatTime;
  getEndTime():string{
    const date = new Date(this.dateService.convertArrayDateToString(this.mission.dateFin));
    // Extract the time using date-fns 'format'
    this.formattedTime = format(date, 'HH:mm'); // 'HH:mm' for 24-hour time format

    return this.formattedTime; // Output: '11:27'
  }
  getStartTime():string{
    const date = new Date(this.dateService.convertArrayDateToString(this.mission.dateDebut));
    // Extract the time using date-fns 'format'
    this.formattedTime = format(date, 'HH:mm'); // 'HH:mm' for 24-hour time format

    return this.formattedTime; // Output: '11:27'
  }

  getStatusIcon(): string {

    //console.log(this.mission.dateDebut);
    const icons = {
      'PLANNED': 'bi-play-circle-fill text-primary',
      'COMPLETED': 'bi-check-circle-fill text-success',
      'CANCELED': 'bi-x-circle-fill text-danger',
      'IN_PROGRESS': 'bi-flag-fill text-info'
    };
    return icons[this.mission?.etatMission as keyof typeof icons] || 'bi-question-circle-fill';
  }

  getDestinationColor(): string {
    return this.mission.deliveryDestinationResponse?.serviceDelivery?.color || '#3498db';
  }

  onEdit() {
   // this.edit.emit(this.mission.id);
  }

  getNumbersOfDrivers(): number{
    if(!this.mission.missionVehiclesDtos){
      return 0;
    }
    return this.mission.missionVehiclesDtos.reduce((total,vehicle)=>{
      const driversCount= vehicle.chauffeursVehicleDto?.length || 0;
      return total + driversCount;
    },0);
  }


  handleMissionUpdate(updatedMission: MissionDto) {
    // Update mission logic
    console.log('Updated mission:', updatedMission);
  }
  ngOnInit(): void {
    this.loadData();
  }


  private loadMission() {
    console.log("reload mission");
    this.missionService.findById7({id:this.mission.id as number}).subscribe(
      {
        next:(m)=>{
          this.mission=m;
        },
        error:(err)=>{
          console.log(err);
        }
      }
    )
  }
  openEditMissionModal(mission: MissionResponse) {
    const modalRef = this.modalService.open(EditMissionModalComponent, {
      size: 'xl',
      centered: true,
      backdrop: 'static'
    });

    // Pass data to modal
    modalRef.componentInstance.mission = convertResponseToDto(mission);
    modalRef.componentInstance.vehicles =this.availableVehicles;
    modalRef.componentInstance.drivers= this.availableDrivers;

    // Handle results
    modalRef.result.then((updatedMission: MissionResponse)=>{
      console.log("from openmodalmethod : "+updatedMission);
      if(updatedMission){
        console.log('Updated mission:', updatedMission);
        this.loadMission();
        // Add your update logic here
      }
    },(reason)=>{
      console.log('Modal dismissed:', reason);
    });
  }
}
