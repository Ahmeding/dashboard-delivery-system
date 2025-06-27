import {Component, Input} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ClientResponse} from "../../../../../../ds-api/src/models/client-response";

@Component({
  selector: 'app-view-client-modal',
  templateUrl: './view-client-modal.component.html',
  styleUrls: ['./view-client-modal.component.scss']
})
export class ViewClientModalComponent {
  @Input() selectedClient!: ClientResponse;  // Accept selectedClient as input from parent
  constructor(public activeModal: NgbActiveModal) {
  }  // Use NgbActiveModal to close modal from within

}
