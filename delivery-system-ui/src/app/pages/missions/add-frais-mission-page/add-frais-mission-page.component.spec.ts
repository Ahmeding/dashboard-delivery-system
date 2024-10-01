import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFraisMissionPageComponent } from './add-frais-mission-page.component';

describe('AddFraisMissionPageComponent', () => {
  let component: AddFraisMissionPageComponent;
  let fixture: ComponentFixture<AddFraisMissionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddFraisMissionPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddFraisMissionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
