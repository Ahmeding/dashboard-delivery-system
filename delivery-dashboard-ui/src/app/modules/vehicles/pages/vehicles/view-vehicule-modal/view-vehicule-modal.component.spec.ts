import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewVehiculeModalComponent } from './view-vehicule-modal.component';

describe('ViewVehiculeModalComponent', () => {
  let component: ViewVehiculeModalComponent;
  let fixture: ComponentFixture<ViewVehiculeModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewVehiculeModalComponent]
    });
    fixture = TestBed.createComponent(ViewVehiculeModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
