import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {DeliveryServiceResponse} from "../../../../../../ds-api/src/models/delivery-service-response";


@Component({
  selector: 'app-new-weekly-planning-modal',
  templateUrl: './new-weekly-planning-modal.component.html',
  styleUrls: ['./new-weekly-planning-modal.component.scss']
})
export class NewWeeklyPlanningModalComponent implements OnInit{
  wPlanningForm!: FormGroup;
  allDestinations: DeliveryServiceResponse[]=[];

  @Input()
  dateDay!: Date;

  constructor(
              public activeModal: NgbActiveModal,
              private fb: FormBuilder) {

  }


  saveWeeklyPlanning() {

  }


  ngOnInit(): void {}








  // Helper methods
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
}
