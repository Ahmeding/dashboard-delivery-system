import {Component, Input} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {VehicleResponse} from "../../../../../../ds-api/src/models/vehicle-response";

@Component({
  selector: 'app-view-vehicule-modal',
  templateUrl: './view-vehicule-modal.component.html',
  styleUrls: ['./view-vehicule-modal.component.scss']
})
export class ViewVehiculeModalComponent {
  @Input() selectedVehicle!: VehicleResponse;  // Accept selectedVehicle as input from parent
  constructor(public activeModal: NgbActiveModal) {
  }  // Use NgbActiveModal to close modal from within

}
