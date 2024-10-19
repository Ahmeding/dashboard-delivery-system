import { Component } from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
interface Vehicle {
  id: number;
  name: string;
}

interface Driver {
  id: number;
  name: string;
}
@Component({
  selector: 'app-add-mission',
  templateUrl: './add-mission.component.html',
  styleUrls: ['./add-mission.component.scss']
})
export class AddMissionComponent {
  availableVehicles: Vehicle[] = [
    { id: 104, name: 'Vehicle A' },
    { id: 105, name: 'Vehicle B' },
    { id: 106, name: 'Vehicle C' },
  ];

  availableDrivers: Driver[] = [
    { id: 1, name: 'Driver X' },
    { id: 2, name: 'Driver Y' },
    { id: 3, name: 'Driver Z' },
    { id: 4, name: 'Driver W' },
  ];

  selectedVehicles: { vehicle: Vehicle; drivers: Driver[] }[] = [];
  selectedVehicle?: Vehicle;
  selectedDrivers: Driver[] = [];

  addVehicle() {
    if (this.selectedVehicle && this.selectedDrivers.length <= 2) {
      this.selectedVehicles.push({
        vehicle: this.selectedVehicle,
        drivers: [...this.selectedDrivers] // Cloning to avoid reference issues
      });
      // Clear selections after adding
      this.selectedVehicle = undefined;
      this.selectedDrivers = [];
    }
  }

  removeVehicle(index: number) {
    this.selectedVehicles.splice(index, 1);
  }

  onSubmit() {
    console.log('Mission data:', this.selectedVehicles);
  }
}
