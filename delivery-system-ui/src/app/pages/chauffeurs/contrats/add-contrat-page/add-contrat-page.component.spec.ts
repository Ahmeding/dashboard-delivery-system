import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddContratPageComponent } from './add-contrat-page.component';

describe('AddContratPageComponent', () => {
  let component: AddContratPageComponent;
  let fixture: ComponentFixture<AddContratPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddContratPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddContratPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
