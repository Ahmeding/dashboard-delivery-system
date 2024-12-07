import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DeliveryDestinationDto} from "../../../../../../ds-api/src/models/delivery-destination-dto";
import {ServiceDeliveryDto} from "../../../../../../ds-api/src/models/service-delivery-dto";

@Component({
  selector: 'app-new-weekly-planning-modal',
  templateUrl: './new-weekly-planning-modal.component.html',
  styleUrls: ['./new-weekly-planning-modal.component.scss']
})
export class NewWeeklyPlanningModalComponent implements OnInit{
  wPlanningForm!: FormGroup;
  allDestinations: ServiceDeliveryDto[]=[];


  constructor(
    public activeModal: NgbActiveModal,
    private fb: FormBuilder)  {
  }

  saveWeeklyPlanning() {

  }

  ngOnInit(): void {
    // Initialize the form
    this.wPlanningForm = this.fb.group({
      title: [null, Validators.required],
      delveryDestination: [null, Validators.required],
      // Initialize the checkboxes as form controls with default value
      day1: [false],
      day2: [false],
      day3: [false],
      day4: [false],
      day5: [false],
      day6: [false],
      day7: [false],
    });
  }
}
