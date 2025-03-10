import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MissionCard } from '../models/mission.model';
import {MissionDto} from "../../../../../../../ds-api/src/models/mission-dto";
import {MissingFileReplacementException} from "@angular-devkit/build-angular/src/utils";
import {MissionResponse} from "../../../../../../../ds-api/src/models/mission-response";
import {formatTime} from "../utils/planning.utils";
import {format} from "date-fns";
import {DateUtilsService} from "../../../../../../services/date-utils/date-utils.service";

@Component({
  selector: 'app-mission-card',
  templateUrl: './mission-card.component.html',
  styleUrls: ['./mission-card.component.scss']
})
export class MissionCardComponent {
  @Input() mission!: MissionResponse/*MissionCard*/;
  @Output() edit = new EventEmitter<number>();
  @Output() delete = new EventEmitter<number>();
  formattedTime: string = '';
  formatTime =formatTime;
  constructor(private dateService:DateUtilsService) {
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
      'EN_COURS': 'bi-play-circle-fill text-primary',
      'ACCOMPLI': 'bi-check-circle-fill text-success',
      'ANNULEE': 'bi-x-circle-fill text-danger',
      'TERMINIEE': 'bi-flag-fill text-info'
    };
    return icons[this.mission?.etatMission as keyof typeof icons] || 'bi-question-circle-fill';
  }

  getDestinationColor(): string {
    return this.mission.deliveryDestinationResponse?.serviceDelivery?.color || '#3498db';
  }

  onEdit() {
    this.edit.emit(this.mission.id);
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
}
