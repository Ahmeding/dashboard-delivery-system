import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditMissionModalComponent } from './edit-mission-modal.component';

describe('EditMissionModalComponent', () => {
  let component: EditMissionModalComponent;
  let fixture: ComponentFixture<EditMissionModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditMissionModalComponent]
    });
    fixture = TestBed.createComponent(EditMissionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
