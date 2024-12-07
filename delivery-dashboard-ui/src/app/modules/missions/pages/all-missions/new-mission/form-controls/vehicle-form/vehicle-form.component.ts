import {Component, Input} from '@angular/core';
import {FormArray, FormGroup} from "@angular/forms";
import {VehiculeDto} from "../../../../../../../../ds-api/src/models/vehicule-dto";
import {ChauffeurDto} from "../../../../../../../../ds-api/src/models/chauffeur-dto";

@Component({
  selector: 'app-vehicle-form',
  templateUrl: './vehicle-form.component.html',
  styleUrls: ['./vehicle-form.component.scss']
})
export class VehicleFormComponent {
  @Input() parentForm!: FormGroup;
  @Input() availableVehicles: VehiculeDto[] = [];
  @Input() availableDrivers: ChauffeurDto[] = [];

  get missionVehiclesDtos() {
    return this.parentForm.get('missionVehiclesDtos') as FormArray;
  }
}
