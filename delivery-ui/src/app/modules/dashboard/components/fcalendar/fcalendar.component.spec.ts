import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FcalendarComponent } from './fcalendar.component';

describe('FcalendarComponent', () => {
  let component: FcalendarComponent;
  let fixture: ComponentFixture<FcalendarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FcalendarComponent]
    });
    fixture = TestBed.createComponent(FcalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
