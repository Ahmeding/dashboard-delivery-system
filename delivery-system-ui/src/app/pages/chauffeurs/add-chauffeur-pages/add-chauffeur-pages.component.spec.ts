import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddChauffeurPagesComponent } from './add-chauffeur-pages.component';

describe('AddChauffeurPagesComponent', () => {
  let component: AddChauffeurPagesComponent;
  let fixture: ComponentFixture<AddChauffeurPagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddChauffeurPagesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddChauffeurPagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
