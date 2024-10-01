import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehiculePageComponent } from './vehicule-page.component';

describe('VehiculePageComponent', () => {
  let component: VehiculePageComponent;
  let fixture: ComponentFixture<VehiculePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VehiculePageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VehiculePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
