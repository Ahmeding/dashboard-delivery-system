import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewRepaireComponent } from './new-repaire.component';

describe('NewRepaireComponent', () => {
  let component: NewRepaireComponent;
  let fixture: ComponentFixture<NewRepaireComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewRepaireComponent]
    });
    fixture = TestBed.createComponent(NewRepaireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
