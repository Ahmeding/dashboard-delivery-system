import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FraisMissionPageComponent } from './frais-mission-page.component';

describe('FraisMissionPageComponent', () => {
  let component: FraisMissionPageComponent;
  let fixture: ComponentFixture<FraisMissionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FraisMissionPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FraisMissionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
