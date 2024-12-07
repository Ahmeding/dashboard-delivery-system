import {Component, Input} from '@angular/core';
import {FormArray, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-stage-form',
  templateUrl: './stage-form.component.html',
  styleUrls: ['./stage-form.component.scss']
})
export class StageFormComponent {
  @Input() parentForm!: FormGroup;
  @Input() deliveryPointsByCity: any[] = [];

  get etapeDtos() {
    return this.parentForm.get('etapeDtos') as FormArray;
  }

  onDeliveryPointChange(value: string) {
    // Implement delivery point change logic
  }
}
