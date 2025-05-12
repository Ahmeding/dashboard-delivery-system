import {Component, Input} from '@angular/core';
import {MissionDto} from "../../../../../../../ds-api/src/models/mission-dto";
import {DayPlan} from "../models/mission.model";
import {isToday, startOfDay} from "date-fns";

@Component({
  selector: 'app-status-card',
  templateUrl: './status-card.component.html',
  styleUrls: ['./status-card.component.scss']
})
export class StatusCardComponent {

  @Input()
  weekDaysForWeekStatus: DayPlan[] = [];

  onFilterChange($event: Event) {

  }

  get currentDayCompleted():number{
    if(!this.weekDaysForWeekStatus) return 0;
    const currentDay= this.weekDaysForWeekStatus.find(day=>isToday(startOfDay(day.date)));
    return currentDay?.missions.filter(mission=>
    mission.etatMission==="COMPLETED").length||0;
  }
  get currentDayOnTrack(): number {
    if (!this.weekDaysForWeekStatus) return 0;
    const currentDay = this.weekDaysForWeekStatus.find(day =>
      isToday(startOfDay(day.date))
    );
    return currentDay?.missions?.filter(mission =>
      mission.etatMission === 'PLANNED' // Update with your actual status property
    ).length || 0;
  }
  get totalWeeklyMissions(): number {
    if (!this.weekDaysForWeekStatus) return 0;
    return this.weekDaysForWeekStatus.reduce(
      (total, day) => total + (day.missions?.length || 0),
      0
    );
  }
  get completionPercentage(): number {
    if (this.currentDayMissions === 0) return 0;
    return Math.round(
      (this.currentDayOnTrack / this.totalWeeklyMissions) * 100
    );
  }

  get currentDayDelayed(): number {
    if (!this.weekDaysForWeekStatus) return 0;
    const currentDay = this.weekDaysForWeekStatus.find(day =>
      isToday(startOfDay(day.date))
    );
    return currentDay?.missions?.filter(mission =>
      mission.etatMission === 'IN_PROGRESS' // Update with your actual status property
    ).length || 0;
  }
  get currentDayMissions():number{
    if(!this.weekDaysForWeekStatus) return 0;
    const today = startOfDay(new Date());
    const currentDay= this.weekDaysForWeekStatus.find(
      day=>isToday(startOfDay(day.date))
    );

    return currentDay?.missions?.length||0;
  }
}
