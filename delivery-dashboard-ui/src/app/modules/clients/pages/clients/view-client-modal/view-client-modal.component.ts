import {Component, Input} from '@angular/core';
import {ChauffeurDto} from "../../../../../../ds-api/src/models/chauffeur-dto";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ClientDto} from "../../../../../../ds-api/src/models/client-dto";

@Component({
  selector: 'app-view-client-modal',
  templateUrl: './view-client-modal.component.html',
  styleUrls: ['./view-client-modal.component.scss']
})
export class ViewClientModalComponent {
  @Input() selectedClient!: ClientDto;  // Accept selectedClient as input from parent
  constructor(public activeModal: NgbActiveModal) {
  }  // Use NgbActiveModal to close modal from within

}
