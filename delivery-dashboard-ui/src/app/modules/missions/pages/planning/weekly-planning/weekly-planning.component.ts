import {Component, OnInit} from '@angular/core';
import {addDays, format, startOfWeek, isToday, isBefore, startOfDay} from 'date-fns';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NewWeeklyPlanningModalComponent} from "../new-weekly-planning-modal/new-weekly-planning-modal.component";
import {DayPlan, MissionCard} from "./models/mission.model";

import {formatDate, formatDateDay, formatDateTimeDay, formatDayName} from './utils/planning.utils';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MissionService} from "../../../../../../ds-api/src/services/mission.service";
import {Router} from "@angular/router";
import {DateUtilsService} from "../../../../../services/date-utils/date-utils.service";
import {MissionResponse} from "../../../../../../ds-api/src/models/mission-response";



@Component({
  selector: 'app-weekly-planning',
  templateUrl: './weekly-planning.component.html',
  styleUrls: ['./weekly-planning.component.scss']
})
export class WeeklyPlanningComponent implements  OnInit {

  weekDays: DayPlan[] = [];
  isLoading = false;
  currentWeekStart: Date = startOfWeek(new Date(), { weekStartsOn: 0 });
  missionsPerWeek!:MissionResponse[];

  constructor(
    private dateService: DateUtilsService, private fb:FormBuilder,
              private missionService:MissionService,
              private router:Router,
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

        this.missionService.findAll7().subscribe({
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
    // Filter missions based on search text
    const filteredMissions = missions.filter(mission =>
      mission.titre?.toLowerCase().includes(this.filterText)
    );
    // Distribute missions to appropriate days
    /*filteredMissions.forEach(mission => {
      const missionDate = new Date(this.dateService.convertArrayDateToString(mission.dateDebut));
      const dayIndex = this.weekDays.findIndex(day =>
        day.date.toDateString() === missionDate.toDateString()
      );
      if (dayIndex !== -1) {
        this.weekDays[dayIndex].missions.push(mission);
      }
    });*/
    // Distribute missions to appropriate days
    missions.forEach(mission => {
      //const missionDate = new Date(this.dateService.convertArrayDateToString(mission.dateDebut));
   if(mission.dateDebut){
     const missionDate = new Date(mission.dateDebut);
     const dayIndex = this.weekDays.findIndex(day =>
       day.date.toDateString() === missionDate.toDateString()
     );
     if (dayIndex !== -1) {
       this.weekDays[dayIndex].missions.push(mission);
     }
   }
    });

    // Sort missions by time for each day
    this.weekDays.forEach(day => {
      day.missions.sort((a, b) =>
        new Date(a.dateDebut ?? '').getTime() -
        new Date(b.dateDebut?? '').getTime()
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
  filterText: string = '';
  onFilterChange($event: Event) {
    this.filterText = (event?.target as HTMLInputElement).value.toLowerCase();
    this.distributeMissions(this.missionsPerWeek);
  }

}
