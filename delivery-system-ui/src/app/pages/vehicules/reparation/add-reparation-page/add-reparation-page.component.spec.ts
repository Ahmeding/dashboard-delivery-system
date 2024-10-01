import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReparationPageComponent } from './add-reparation-page.component';

describe('AddReparationPageComponent', () => {
  let component: AddReparationPageComponent;
  let fixture: ComponentFixture<AddReparationPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddReparationPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddReparationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
