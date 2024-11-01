import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServicesDeleiveryComponent } from './services-deleivery.component';

describe('ServicesDeleiveryComponent', () => {
  let component: ServicesDeleiveryComponent;
  let fixture: ComponentFixture<ServicesDeleiveryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ServicesDeleiveryComponent]
    });
    fixture = TestBed.createComponent(ServicesDeleiveryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
