import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-event-modal',
  templateUrl: './event-modal.component.html',
  styleUrls: ['./event-modal.component.scss']
})
export class EventModalComponent implements OnInit {
  @Input() event: any = null;
  @Input() date: Date = new Date();

  eventForm: FormGroup = this.initForm();
  isEditMode = false;

  constructor(
    public activeModal: NgbActiveModal,
    private fb: FormBuilder
  ) {}

  private initForm(): FormGroup {
    return this.fb.group({
      title: ['', Validators.required],
      start: [new Date(), Validators.required],
      end: [new Date()],
      backgroundColor: ['#3498db'],
      description: [''],
      location: ['']
    });
  }

  ngOnInit() {
    this.isEditMode = !!this.event;

    if (this.isEditMode) {
      this.eventForm.patchValue({
        title: this.event.title,
        start: this.event.start,
        end: this.event.end || this.event.start,
        backgroundColor: this.event.backgroundColor,
        description: this.event.extendedProps?.description || '',
        location: this.event.extendedProps?.location || ''
      });
    } else if (this.date) {
      this.eventForm.patchValue({
        start: this.date,
        end: this.date
      });
    }
  }

  onSubmit() {
    if (this.eventForm.valid) {
      this.activeModal.close(this.eventForm.value);
    }
  }

  onDelete() {
    if (this.isEditMode) {
      this.activeModal.close({ delete: true });
    }
  }
}
