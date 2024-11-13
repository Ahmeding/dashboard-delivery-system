import {Component, Input} from '@angular/core';
import {ContratDto} from "../../../../../../ds-api/src/models/contrat-dto";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-view-contract-modal',
  templateUrl: './view-contract-modal.component.html',
  styleUrls: ['./view-contract-modal.component.scss']
})
export class ViewContractModalComponent {
    @Input() selectedContract!:ContratDto;
  constructor(public activeModal: NgbActiveModal) {
  }  // Use NgbActiveModal to close modal from within
}
