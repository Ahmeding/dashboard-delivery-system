import { Component } from '@angular/core';
import {VehiculeDto} from "../../../../../../ds-api/src/models/vehicule-dto";

@Component({
  selector: 'app-new-vehicle',
  templateUrl: './new-vehicle.component.html',
  styleUrls: ['./new-vehicle.component.scss']
})
export class NewVehicleComponent {
  newVehicle: VehiculeDto={};
  isFormAddNewSubmitted: boolean | undefined;

}
