import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewRepairComponent } from './new-repair.component';

describe('NewRepairComponent', () => {
  let component: NewRepairComponent;
  let fixture: ComponentFixture<NewRepairComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewRepairComponent]
    });
    fixture = TestBed.createComponent(NewRepairComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
