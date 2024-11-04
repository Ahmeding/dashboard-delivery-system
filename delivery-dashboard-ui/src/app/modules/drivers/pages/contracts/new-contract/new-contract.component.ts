import { Component } from '@angular/core';
import {NgForm} from "@angular/forms";
import {ContratDto} from "../../../../../../ds-api/src/models/contrat-dto";

@Component({
  selector: 'app-new-contract',
  templateUrl: './new-contract.component.html',
  styleUrls: ['./new-contract.component.scss']
})
export class NewContractComponent {
  contract: ContratDto = {};

  onSubmit(contractForm: NgForm) {

  }
}
