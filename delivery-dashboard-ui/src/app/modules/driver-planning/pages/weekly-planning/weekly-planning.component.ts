import {Component, OnInit} from '@angular/core';
import {
  formatDate, formatDateDay,
  formatDateTimeDay,
  formatDayName
} from "../../../missions/pages/planning/weekly-planning/utils/planning.utils";
import {DayPlan, MissionCard} from "../../../missions/pages/planning/weekly-planning/models/mission.model";
import {addDays, format, startOfWeek, isToday, isBefore, startOfDay} from 'date-fns';
import {MissionResponse} from "../../../../../ds-api/src/models/mission-response";
import {DateUtilsService} from "../../../../services/date-utils/date-utils.service";
import {FormBuilder} from "@angular/forms";
import {MissionService} from "../../../../../ds-api/src/services/mission.service";
import {Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {
  NewWeeklyPlanningModalComponent
} from "../../../missions/pages/planning/new-weekly-planning-modal/new-weekly-planning-modal.component";
import {TokenService} from "../../../../services/token/token.service";

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
    private tokenService:TokenService,
    private fb:FormBuilder,
    private missionService:MissionService,
    private router:Router,
    private modalService:NgbModal) {

  }

  isConnectedUserAdmin(){
    return this.tokenService.hasRoles(["ROLE_ADMIN"]);
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

    this.missionService.findAllMissionsByDriver().subscribe({
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
    missions.forEach(mission => {
      const missionDate = new Date(mission.dateDebut!);
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
        new Date(a.dateDebut!).getTime() -
        new Date(b.dateDebut!).getTime()
      );
    });
  }

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
