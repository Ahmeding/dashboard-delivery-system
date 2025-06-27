import {Component, Input} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {DriverResponse} from "../../../../../../ds-api/src/models/driver-response";

@Component({
  selector: 'app-view-driver-modal',
  templateUrl: './view-driver-modal.component.html',
  styleUrls: ['./view-driver-modal.component.scss']
})
export class ViewDriverModalComponent {
  @Input() selectedDriver!: DriverResponse;  // Accept selectedVehicle as input from parent
  constructor(public activeModal: NgbActiveModal) {
  }  // Use NgbActiveModal to close modal from within

}
