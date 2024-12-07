import {Component, OnInit} from '@angular/core';
import {addDays, format, startOfWeek, isToday, isBefore, startOfDay} from 'date-fns';
import {
  NewDpCategoryModalComponent
} from "../../../../settings/pages/delivery-point-category/new-dp-category-modal/new-dp-category-modal.component";
import {AlertService} from "../../../../../services/alert/alert.service";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NewWeeklyPlanningModalComponent} from "../new-weekly-planning-modal/new-weekly-planning-modal.component";

interface Mission {
  id: number;
  title: string;
  startTime: string;
  endTime: string;
  status: 'EN_COURS' | 'ACCOMPLI' | 'ANNULEE' | 'TERMINIEE';
  driver: string;
  vehicle: string;
}

interface DayPlan {
  date: Date;
  missions: Mission[];
}
@Component({
  selector: 'app-weekly-planning',
  templateUrl: './weekly-planning.component.html',
  styleUrls: ['./weekly-planning.component.scss']
})
export class WeeklyPlanningComponent implements  OnInit {
  weekDays: DayPlan[] = [];
  currentWeekStart: Date = startOfWeek(new Date(), { weekStartsOn: 1 });

  constructor(private alertService:AlertService,
              private confirmBoxService:ConfirmBoxEvokeService,
              private modalService:NgbModal) {
    this.initializeWeek();
  }

  ngOnInit() {
    this.loadMissions();
  }

  private initializeWeek() {
    this.weekDays = Array.from({ length: 7 }, (_, i) => ({
      date: addDays(this.currentWeekStart, i),
      missions: []
    }));
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

  addNewMission(date: Date) {
    console.log('Adding new mission for:', format(date, 'yyyy-MM-dd'));
    // Implement your new mission logic here
  }

  isToday(date: Date): boolean {
    return isToday(date);
  }

  formatDate(date: Date): string {
    return format(date, 'MMM dd');
  }

  formatDayName(date: Date): string {
    return format(date, 'EEEE');
  }

  getStatusIcon(status: Mission['status']): string {
    const icons = {
      'EN_COURS': 'bi-play-circle-fill text-primary',
      'ACCOMPLI': 'bi-check-circle-fill text-success',
      'ANNULEE': 'bi-x-circle-fill text-danger',
      'TERMINIEE': 'bi-flag-fill text-info'
    };
    return icons[status] || 'bi-question-circle-fill';
  }

  private loadMissions() {
    // Simulated data - replace with actual API call
    const mockMissions: Mission[] = [
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
    ];

    // Distribute missions across days (simulate real data distribution)
    this.weekDays.forEach(day => {
      day.missions = mockMissions.filter(() => Math.random() > 0.5);
    });
  }

  isNotOldDay(date: Date):boolean {
    const currentDate= startOfDay(new Date());
    const inputDate = startOfDay(date);
    return !isBefore(inputDate,currentDate) //date>=currentDate()
  }

  openModalNewWeeklyPlanning() {
    const modalRef =this.modalService.open(NewWeeklyPlanningModalComponent,{
      size:'xl',
      centered:true,
      backdrop:'static'
    });

    modalRef.result.then((result)=>{
      if(result){
        console.log("new weekly Planning");
      }
    }).catch((error)=>{
      console.log("weekly Planning Modale dismissed");
    });
  }
}
