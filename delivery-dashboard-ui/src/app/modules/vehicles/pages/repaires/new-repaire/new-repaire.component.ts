import { Component } from '@angular/core';
import {ReparationDto} from "../../../../../../ds-api/src/models/reparation-dto";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-new-repaire',
  templateUrl: './new-repaire.component.html',
  styleUrls: ['./new-repaire.component.scss']
})
export class NewRepaireComponent {
  repair: ReparationDto = {};

    onSubmit(repairForm: NgForm) {

    }
}
