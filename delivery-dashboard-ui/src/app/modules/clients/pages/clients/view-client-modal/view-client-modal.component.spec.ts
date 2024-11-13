import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewClientModalComponent } from './view-client-modal.component';

describe('ViewClientModalComponent', () => {
  let component: ViewClientModalComponent;
  let fixture: ComponentFixture<ViewClientModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewClientModalComponent]
    });
    fixture = TestBed.createComponent(ViewClientModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
