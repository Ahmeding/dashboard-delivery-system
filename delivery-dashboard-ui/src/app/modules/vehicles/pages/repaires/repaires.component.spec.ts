import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepairesComponent } from './repaires.component';

describe('RepairesComponent', () => {
  let component: RepairesComponent;
  let fixture: ComponentFixture<RepairesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RepairesComponent]
    });
    fixture = TestBed.createComponent(RepairesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
