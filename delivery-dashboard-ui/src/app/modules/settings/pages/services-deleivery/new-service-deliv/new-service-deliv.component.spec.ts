import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewServiceDelivComponent } from './new-service-deliv.component';

describe('NewServiceDelivComponent', () => {
  let component: NewServiceDelivComponent;
  let fixture: ComponentFixture<NewServiceDelivComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewServiceDelivComponent]
    });
    fixture = TestBed.createComponent(NewServiceDelivComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
