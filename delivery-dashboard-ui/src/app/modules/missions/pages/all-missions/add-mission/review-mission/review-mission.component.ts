import {Component, Input} from '@angular/core';
import {MissionDto} from "../../../../../../../ds-api/src/models/mission-dto";

@Component({
  selector: 'app-review-mission',
  templateUrl: './review-mission.component.html',
  styleUrls: ['./review-mission.component.scss']
})
export class ReviewMissionComponent {
  @Input() missionData!: MissionDto;
}
