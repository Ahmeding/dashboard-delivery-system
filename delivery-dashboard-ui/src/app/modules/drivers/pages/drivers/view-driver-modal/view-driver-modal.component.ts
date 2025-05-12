import {Component, Input} from '@angular/core';
import {VehicleDto} from "../../../../../../ds-api/src/models/vehicle-dto";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ChauffeurDto} from "../../../../../../ds-api/src/models/chauffeur-dto";

@Component({
  selector: 'app-view-driver-modal',
  templateUrl: './view-driver-modal.component.html',
  styleUrls: ['./view-driver-modal.component.scss']
})
export class ViewDriverModalComponent {
  @Input() selectedDriver!: ChauffeurDto;  // Accept selectedVehicle as input from parent
  constructor(public activeModal: NgbActiveModal) {
  }  // Use NgbActiveModal to close modal from within

}
