import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMissionComponent } from './add-mission.component';

describe('AddMissionComponent', () => {
  let component: AddMissionComponent;
  let fixture: ComponentFixture<AddMissionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddMissionComponent]
    });
    fixture = TestBed.createComponent(AddMissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
