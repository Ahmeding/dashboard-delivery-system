import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDeliveryPointComponent } from './new-delivery-point.component';

describe('NewDeliveryPointComponent', () => {
  let component: NewDeliveryPointComponent;
  let fixture: ComponentFixture<NewDeliveryPointComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewDeliveryPointComponent]
    });
    fixture = TestBed.createComponent(NewDeliveryPointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
