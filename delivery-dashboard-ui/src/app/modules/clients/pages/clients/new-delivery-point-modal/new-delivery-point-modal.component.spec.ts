import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDeliveryPointModalComponent } from './new-delivery-point-modal.component';

describe('NewDeliveryPointModalComponent', () => {
  let component: NewDeliveryPointModalComponent;
  let fixture: ComponentFixture<NewDeliveryPointModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewDeliveryPointModalComponent]
    });
    fixture = TestBed.createComponent(NewDeliveryPointModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
