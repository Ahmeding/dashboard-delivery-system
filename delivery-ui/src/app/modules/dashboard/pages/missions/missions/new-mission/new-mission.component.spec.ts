import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewMissionComponent } from './new-mission.component';

describe('NewMissionComponent', () => {
  let component: NewMissionComponent;
  let fixture: ComponentFixture<NewMissionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewMissionComponent]
    });
    fixture = TestBed.createComponent(NewMissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
