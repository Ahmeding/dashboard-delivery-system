import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContratPageComponent } from './contrat-page.component';

describe('ContratPageComponent', () => {
  let component: ContratPageComponent;
  let fixture: ComponentFixture<ContratPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ContratPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ContratPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
