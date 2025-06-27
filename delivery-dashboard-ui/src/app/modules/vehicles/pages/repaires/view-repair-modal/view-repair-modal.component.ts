import {Component, Input} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-view-repair-modal',
  templateUrl: './view-repair-modal.component.html',
  styleUrls: ['./view-repair-modal.component.scss']
})
export class ViewRepairModalComponent {
  @Input()
  selectedRepair: any;  // Accept selectedVehicle as input from parent
  constructor(public activeModal: NgbActiveModal) {
  }
}
