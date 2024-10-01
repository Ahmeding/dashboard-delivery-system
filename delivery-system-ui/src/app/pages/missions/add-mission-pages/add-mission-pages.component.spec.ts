import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMissionPagesComponent } from './add-mission-pages.component';

describe('AddMissionPagesComponent', () => {
  let component: AddMissionPagesComponent;
  let fixture: ComponentFixture<AddMissionPagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddMissionPagesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddMissionPagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
