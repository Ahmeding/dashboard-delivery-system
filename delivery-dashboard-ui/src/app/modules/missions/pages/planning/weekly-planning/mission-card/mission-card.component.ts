import {Component, Input, Output, EventEmitter, ViewChild, OnInit, HostListener} from '@angular/core';
import {MissionResponse} from "../../../../../../../ds-api/src/models/mission-response";
import {formatTime} from "../utils/planning.utils";
import {format, parseISO} from "date-fns";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MissionService} from "../../../../../../../ds-api/src/services/mission.service";
import {EditMissionModalComponent} from "../../edit-mission-modal/edit-mission-modal.component";
import {
  ManageTripMissionModalComponent
} from "../../../../../driver-planning/pages/components/manage-trip-mission-modal/manage-trip-mission-modal.component";
import {TokenService} from "../../../../../../services/token/token.service";
import {Router} from "@angular/router";
import {DateHelperService} from "../../../../../../components/shared/services/date-helper/date-helper.service";
import {MenuMissionCardService} from "../../../../../../components/shared/menu/menu-mission-card.service";
import {VehicleResponse} from "../../../../../../../ds-api/src/models/vehicle-response";
import {DriverResponse} from "../../../../../../../ds-api/src/models/driver-response";
import {DriverService} from "../../../../../../../ds-api/src/services/driver.service";
import {VehicleService} from "../../../../../../../ds-api/src/services/vehicle.service";
import {MissionRequest} from "../../../../../../../ds-api/src/models/mission-request";

@Component({
  selector: 'app-mission-card',
  templateUrl: './mission-card.component.html',
  styleUrls: ['./mission-card.component.scss']
})
export class MissionCardComponent implements OnInit{


  @Input() mission!: MissionResponse/*MissionCard*/;
  currentOpenMenuId:number | null = null;

  @Output() edit = new EventEmitter<number>();
  @Output() delete = new EventEmitter<number>();
  @ViewChild(EditMissionModalComponent) editModal!: EditMissionModalComponent;

  // Load these from your service
  availableVehicles: VehicleResponse[] = [];
  availableDrivers: DriverResponse[] = [];


  constructor(
    private menuService: MenuMissionCardService,
    private dateDayService: DateHelperService,
    private tokenService: TokenService,
    private missionService: MissionService,
    private modalService: NgbModal,
              private vehicleService:VehicleService,
              private driverService:DriverService){
    this.menuService.openMenuId$.subscribe(openId=>{
      this.currentOpenMenuId = openId;
    })
  }


  private loadAvailableVehicles(){
    this.vehicleService.findAllVehicle().subscribe(
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
this.driverService.findAllDriver().subscribe({
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

  getStartTime():string{
    const date = new Date(this.mission.startDate!);
    // Extract the time using date-fns 'format'
    this.formattedTime = format(date, 'HH:mm');
    return this.formattedTime;
  }

  getStatusIcon(): string {

    //console.log(this.mission.dateDebut);
    const icons = {
      'PLANNED': 'bi-play-circle-fill text-primary',
      'COMPLETED': 'bi-check-circle-fill text-success',
      'CANCELED': 'bi-x-circle-fill text-danger',
      'IN_PROGRESS': 'bi-flag-fill text-info'
    };
    return icons[this.mission?.missionStatus as keyof typeof icons] || 'bi-question-circle-fill';
  }

  getDestinationColor(): string {
    return this.mission.deliveryDestination?.deliveryService?.color || '#3498db';
  }


  ngOnInit(): void {
    this.loadData();
  }


  private reloadMission() {
    console.log("reload mission");
    this.missionService.findMissionById({id:this.mission.id as number}).subscribe(
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
    const missionToEdit: MissionResponse = mission;
    if(this.tokenService.hasRoles(["ROLE_ADMIN"])){
    this.editByAdmin(missionToEdit);
    }
  }

  private editByAdmin(missionToEdit:MissionResponse){
    const modalRef = this.modalService.open(EditMissionModalComponent, {
      size: 'xl',
      centered: true,
      backdrop: 'static'
    });
    // Pass data to modal
    modalRef.componentInstance.mission = missionToEdit;
    modalRef.componentInstance.vehicles =this.availableVehicles;
    modalRef.componentInstance.drivers= this.availableDrivers;
    // Handle results
    modalRef.result.then(
      (updatedMission: MissionResponse)=> {
        console.log("from openmodalmethod : "+updatedMission);
        if(updatedMission){
          console.log('Updated mission:', updatedMission);
          this.reloadMission();
          // Add your update logic here
        }
      },
      (reason)=>{
        console.log('Modal dismissed:', reason);
      });
  }

  private editByDriver(missionToEdit:MissionResponse, isStartEndKm:boolean){
    console.log(isStartEndKm);
    const modalEditingByDriver = this.modalService.open(ManageTripMissionModalComponent
    ,{
      size:'xl',
      centered:true,
      backdrop:'static'
      });
    modalEditingByDriver.componentInstance.mission = missionToEdit;
    modalEditingByDriver.componentInstance.vehicles= this.availableVehicles;
    modalEditingByDriver.componentInstance.drivers= this.availableDrivers;
    modalEditingByDriver.componentInstance.isStartEndKm = isStartEndKm;

    modalEditingByDriver.result.then((updatedMission :MissionResponse)=>{
        console.log("success driver update");
      this.reloadMission();
        if(updatedMission){
        console.log('Updated mission:', updatedMission);
        // Add your update logic here
      }
      },(reason)=>{
      console.log("Modle dissmissed: "+ reason);
      }
    );
  }

  isInputDateSameDay():boolean{
    //console.log("isOutOfDate",this.mission.dateDebut);
    const date = parseISO(this.mission.startDate as string);
    //console.log("isNotOldDay",this.dateDayService.isNotOldDay(date));
    return this.dateDayService.isInputDateSameDay(date);
  }

  isUserAsDriver():boolean {
    return this.tokenService.hasRoles(["ROLE_USER"]);
  }
  //showMenu = false;

  toggelMenu(event: Event, missionId:number) {
    event.stopPropagation();
    const newOpenId= this.currentOpenMenuId === missionId ? null : missionId;
    this.menuService.setOpenMenu(newOpenId)
    //this.showMenu=!this.showMenu;
  }

  @HostListener('document:click', ['$event'])
  closeMenu(event: MouseEvent) {
    const clickedElement = event.target as HTMLElement;
    const isMenuClicked = clickedElement.closest('.menu-container');
    const isButtonClicked = clickedElement.closest('.action-btn');
    if (!isMenuClicked && !isButtonClicked) {
      this.menuService.setOpenMenu(null);
    }
  }

  handleStarEndKm(mission: MissionResponse) {
    const missionToEdit: MissionResponse = mission;

    this.editByDriver(missionToEdit,true);
  }

  handleExpenses(mission: MissionResponse) {

    const missionToEdit: MissionResponse = mission;

    this.editByDriver(missionToEdit,false);
  }
}
