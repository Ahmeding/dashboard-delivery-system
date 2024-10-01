import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVehiculePageComponent } from './add-vehicule-page.component';

describe('AddVehiculePageComponent', () => {
  let component: AddVehiculePageComponent;
  let fixture: ComponentFixture<AddVehiculePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddVehiculePageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddVehiculePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
