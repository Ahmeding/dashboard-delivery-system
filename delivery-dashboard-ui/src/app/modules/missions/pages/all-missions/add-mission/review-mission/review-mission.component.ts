import {Component, Input} from '@angular/core';
import {MissionResponse} from "../../../../../../../ds-api/src/models/mission-response";

@Component({
  selector: 'app-review-mission',
  templateUrl: './review-mission.component.html',
  styleUrls: ['./review-mission.component.scss']
})
export class ReviewMissionComponent {
  @Input() missionData!: MissionResponse;
}
