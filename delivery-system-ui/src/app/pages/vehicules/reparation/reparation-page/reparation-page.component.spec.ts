import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReparationPageComponent } from './reparation-page.component';

describe('ReparationPageComponent', () => {
  let component: ReparationPageComponent;
  let fixture: ComponentFixture<ReparationPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReparationPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReparationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
