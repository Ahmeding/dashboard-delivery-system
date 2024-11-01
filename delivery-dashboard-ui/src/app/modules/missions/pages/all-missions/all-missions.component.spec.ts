import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllMissionsComponent } from './all-missions.component';

describe('AllMissionsComponent', () => {
  let component: AllMissionsComponent;
  let fixture: ComponentFixture<AllMissionsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AllMissionsComponent]
    });
    fixture = TestBed.createComponent(AllMissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
