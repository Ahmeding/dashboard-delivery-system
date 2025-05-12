import {Component, Input} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {VehicleDto} from "../../../../../../ds-api/src/models/vehicle-dto";

@Component({
  selector: 'app-view-vehicule-modal',
  templateUrl: './view-vehicule-modal.component.html',
  styleUrls: ['./view-vehicule-modal.component.scss']
})
export class ViewVehiculeModalComponent {
  @Input() selectedVehicle!: VehicleDto;  // Accept selectedVehicle as input from parent
  constructor(public activeModal: NgbActiveModal) {
  }  // Use NgbActiveModal to close modal from within

}
