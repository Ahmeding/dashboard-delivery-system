import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChauffeurPagesComponent } from './chauffeur-pages.component';

describe('ChauffeurPagesComponent', () => {
  let component: ChauffeurPagesComponent;
  let fixture: ComponentFixture<ChauffeurPagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ChauffeurPagesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ChauffeurPagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
