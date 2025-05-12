import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageTripMissionComponent } from './manage-trip-mission.component';

describe('ManageTripMissionComponent', () => {
  let component: ManageTripMissionComponent;
  let fixture: ComponentFixture<ManageTripMissionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManageTripMissionComponent]
    });
    fixture = TestBed.createComponent(ManageTripMissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
