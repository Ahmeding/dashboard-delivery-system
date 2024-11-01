import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverSpaceComponent } from './driver-space.component';

describe('DriverSpaceComponent', () => {
  let component: DriverSpaceComponent;
  let fixture: ComponentFixture<DriverSpaceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DriverSpaceComponent]
    });
    fixture = TestBed.createComponent(DriverSpaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
