import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewWeeklyPlanningModalComponent } from './new-weekly-planning-modal.component';

describe('NewWeeklyPlanningModalComponent', () => {
  let component: NewWeeklyPlanningModalComponent;
  let fixture: ComponentFixture<NewWeeklyPlanningModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewWeeklyPlanningModalComponent]
    });
    fixture = TestBed.createComponent(NewWeeklyPlanningModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
