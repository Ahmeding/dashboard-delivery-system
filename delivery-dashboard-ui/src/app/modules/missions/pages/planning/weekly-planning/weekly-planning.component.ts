import {Component, OnInit} from '@angular/core';
import {addDays, format, startOfWeek, isToday, isBefore, startOfDay} from 'date-fns';
import {
  NewDpCategoryModalComponent
} from "../../../../settings/pages/delivery-point-category/new-dp-category-modal/new-dp-category-modal.component";
import {AlertService} from "../../../../../services/alert/alert.service";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NewWeeklyPlanningModalComponent} from "../new-weekly-planning-modal/new-weekly-planning-modal.component";
import {WeeklyPlanningDto} from "../../../../../../ds-api/src/models/weekly-planning-dto";
import {DayPlanDto} from "../../../../../../ds-api/src/models/day-plan-dto";
import {WeeklyPlanningService} from "../../../../../../ds-api/src/services/weekly-planning.service";
import {DayPlanService} from "../../../../../../ds-api/src/services/day-plan.service";
import {DayPlan, MissionCard} from "./models/mission.model";
import {PlanningService} from "./services/planning-service";

import {formatDate, formatDateDay, formatDateTimeDay, formatDayName} from './utils/planning.utils';
import {MissionDto} from "../../../../../../ds-api/src/models/mission-dto";
import {EtapeDto} from "../../../../../../ds-api/src/models/etape-dto";
import {VehiculeDto} from "../../../../../../ds-api/src/models/vehicule-dto";
import {ChauffeurDto} from "../../../../../../ds-api/src/models/chauffeur-dto";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {ChauffeurVehicleDto} from "../../../../../../ds-api/src/models/chauffeur-vehicle-dto";
import {MissionVehicleDto} from "../../../../../../ds-api/src/models/mission-vehicle-dto";
import {ServiceDeliveryDto} from "../../../../../../ds-api/src/models/service-delivery-dto";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";
import {ServiceDeliveryService} from "../../../../../../ds-api/src/services/service-delivery.service";
import {MissionService} from "../../../../../../ds-api/src/services/mission.service";
import {DeliveryPointsService} from "../../../../../../ds-api/src/services/delivery-points.service";
import {VehiculeService} from "../../../../../../ds-api/src/services/vehicule.service";
import {ChauffeurService} from "../../../../../../ds-api/src/services/chauffeur.service";
import {Router} from "@angular/router";
import {DateUtilsService} from "../../../../../services/date-utils/date-utils.service";
import {MissionResponse} from "../../../../../../ds-api/src/models/mission-response";
/*
interface Mission {
  id: number;
  title: string;
  startTime: string;
  startDate: string;
  endTime: string;
  status: 'EN_COURS' | 'ACCOMPLI' | 'ANNULEE' | 'TERMINIEE';
  driver: string;
  vehicle: string;
}

interface DayPlan {
  date: Date;
  missions: Mission[];
}
*/


@Component({
  selector: 'app-weekly-planning',
  templateUrl: './weekly-planning.component.html',
  styleUrls: ['./weekly-planning.component.scss']
})
export class WeeklyPlanningComponent implements  OnInit {

  weeklyPlannigToSave:WeeklyPlanningDto={};
  dayPlanToSave:DayPlanDto={};
  testDayPlan:DayPlan={date:new Date,missions:[]}
  testMissions: MissionCard[]=[];
  testMission!:MissionCard;
  weekDays: DayPlan[] = [];
  isLoading = false;
  currentWeekStart: Date = startOfWeek(new Date(), { weekStartsOn: 1 });
  missionsPerWeek!:MissionResponse[];

  constructor(
    private dateService: DateUtilsService, private fb:FormBuilder,
              private missionService:MissionService,
              private planningService: PlanningService,
              private weeklyPlanningService: WeeklyPlanningService,
              private dayPlanService:DayPlanService,
              private alertService:AlertService,
              private router:Router,
              private confirmBoxService:ConfirmBoxEvokeService,
              private modalService:NgbModal) {

  }

  ngOnInit() {
    this.initializeWeek();
    this.loadMissions();
  }
  private initializeWeek() {
    this.weekDays = Array.from({ length: 7 }, (_, i) => ({
      date: addDays(this.currentWeekStart, i),
      missions: []
    }));
  }


  openModalNewWeeklyPlanning(dateDay: Date) {
    const modalRef =this.modalService.open(NewWeeklyPlanningModalComponent,{
      size:'xl',
      centered:true,
      backdrop:'static'
    });
    modalRef.componentInstance.dateDay=dateDay
    modalRef.result.then((result)=>{
      if(result){
        console.log("new weekly Planning");
      }
    }).catch((error)=>{
      console.log("weekly Planning Modale dismissed");
    });
  }



  addNewMission(date: Date) {
    // Navigate to new mission form with pre-filled date
    console.log('Adding new mission for:', formatDate(date));
  }

  onEditMission(missionId: number) {
    console.log('Editing mission:', missionId);
    // Implement edit logic
  }
  onDeleteMission(missionId: number) {
    console.log('Deleting mission:', missionId);
    // Implement delete logic
  }

  // Utility methods
  formatDateTimeDay= formatDateTimeDay
  formatDateDay= formatDateDay;
  formatDate = formatDate;
  formatDayName = formatDayName;

  private loadMissions() {

    this.isLoading = true;

/*    this.planningService.getMissionsByWeek(this.currentWeekStart).subscribe({
        next: (missions) => {
          console.log(missions.length);
          this.distributeMissions(missions);
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading missions:', error);
          this.isLoading = false;
        }
      });*/

        this.missionService.findAll6().subscribe({
          next:(allMissions)=>{

            console.log(allMissions.length);
            this.missionsPerWeek=allMissions;
            this.distributeMissions(allMissions);
            this.isLoading = false;
          },
          error:(error)=>{
            console.error('Error loading missions:', error);
            this.isLoading = false;
        }
        });

  }

  private distributeMissions(missions: MissionResponse[]) {
    // Reset missions for each day
    this.weekDays.forEach(day => day.missions = []);
    // Distribute missions to appropriate days
    missions.forEach(mission => {
      const missionDate = new Date(this.dateService.convertArrayDateToString(mission.dateDebut));
      const dayIndex = this.weekDays.findIndex(day =>
        day.date.toDateString() === missionDate.toDateString()
      );
      if (dayIndex !== -1) {
        this.weekDays[dayIndex].missions.push(mission);
      }
    });

    // Sort missions by time for each day
    this.weekDays.forEach(day => {
      day.missions.sort((a, b) =>
        new Date(this.dateService.convertArrayDateToString(a.dateDebut)).getTime() -
        new Date(this.dateService.convertArrayDateToString(b.dateDebut)).getTime()
      );
    });
  }


  /*
    addNewMission(date: Date) {
  /!*
   console.error('Adding new mission for:', format(date, 'yyyy-MM-dd'));
      console.error('weewlyPlanning Title:', this.formatDate(this.weekDays[0].date) + " - " + this.formatDate(this.weekDays[6].date));
      console.error('weewlyPlanning Start date:', format(this.weekDays[0].date,'dd-MM-yyyy'));
      console.error('weewlyPlanning end date:', format(this.weekDays[6].date,'dd-MM-yyyy'));
      console.error('Math.random() > 0.5:', Math.random() > 0.5);*!/
      const normalizedDate = startOfDay(date);



      console.log("--------------------------> UTC ISO String: ", normalizedDate.toISOString().split('T')[0]);
      console.log("--------------------------> UTC Formatted: ", format(normalizedDate, 'yyyy-MM-dd'));
      // Implement your new mission logic here
      this.testMission ={
        id: 1,
        title: 'Mission Wien',
        startTime: '22:00',
        startDate: format(normalizedDate, 'yyyy-MM-dd'),
        endTime: '14:30',
        status: 'EN_COURS',
        destination: "PARIS",
        destinationColor: "red",
        drivers: [],
        vehicles: []
      }
    this.testMissions.push(this.testMission);
  this.loadMissions();

    }

  */


  previousWeek() {
    this.currentWeekStart = addDays(this.currentWeekStart, -7);
    this.initializeWeek();
    this.loadMissions();
  }

  nextWeek() {
    this.currentWeekStart = addDays(this.currentWeekStart, 7);
    this.initializeWeek();
    this.loadMissions();
  }

  isToday(date: Date): boolean {
    return isToday(date);
  }


  /* private loadMissions() {
     // Simulated data - replace with actual API call
     /!*const mockMissions: Mission[] = [
       {
         id: 1,
         title: 'Mission Lyon',
         startTime: '22:00',
         endTime: '14:30',
         status: 'EN_COURS',
         driver: 'John Doe',
         vehicle: 'VAN-001'
       },
       {
         id: 2,
         title: 'Pickup from Warehouse',
         startTime: '11:00',
         endTime: '12:30',
         status: 'ACCOMPLI',
         driver: 'Jane Smith',
         vehicle: 'TRUCK-002'
       }
     ];*!/

     // Distribute missions across days (simulate real data distribution)
     this.weekDays.forEach(day => {
       //day.missions =  this.testMissions.filter(() => Math.random() > 0.5);
       const dayDateString= format(day.date,'yyyy-MM-dd');//.toISOString().split('T')[0];
       console.log(dayDateString);
       day.missions =  this.testMissions.filter(
         mission =>mission.startDate===dayDateString);
     });

   }
 */
  isNotOldDay(date: Date):boolean {
    const currentDate= startOfDay(new Date());
    const inputDate = startOfDay(date);
    return !isBefore(inputDate,currentDate) //date>=currentDate()
  }
  getStatusIcon(status: MissionCard['status']): string {
    const icons = {
      'EN_COURS': 'bi-play-circle-fill text-primary',
      'ACCOMPLI': 'bi-check-circle-fill text-success',
      'ANNULEE': 'bi-x-circle-fill text-danger',
      'TERMINIEE': 'bi-flag-fill text-info'
    };
    return icons[status] || 'bi-question-circle-fill';
  }


  navigateToNewMissionByDate(dateDay: Date) {

    const dateDayString= formatDateDay(dateDay);
    console.log("---- "+dateDayString+" ----");
    this.router.navigate(['missions',"add-mission",dateDayString]);
  }
}
