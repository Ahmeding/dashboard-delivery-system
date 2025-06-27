import {Component, Input} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {EmploymentContractResponse} from "../../../../../../ds-api/src/models/employment-contract-response";

@Component({
  selector: 'app-view-contract-modal',
  templateUrl: './view-contract-modal.component.html',
  styleUrls: ['./view-contract-modal.component.scss']
})
export class ViewContractModalComponent {
    @Input() selectedContract!:EmploymentContractResponse;
  constructor(public activeModal: NgbActiveModal) {
  }  // Use NgbActiveModal to close modal from within
}
