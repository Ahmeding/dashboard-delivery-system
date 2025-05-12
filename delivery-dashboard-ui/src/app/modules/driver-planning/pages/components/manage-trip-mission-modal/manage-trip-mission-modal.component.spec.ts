import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageTripMissionModalComponent } from './manage-trip-mission-modal.component';

describe('ManageTripMissionModalComponent', () => {
  let component: ManageTripMissionModalComponent;
  let fixture: ComponentFixture<ManageTripMissionModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManageTripMissionModalComponent]
    });
    fixture = TestBed.createComponent(ManageTripMissionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
