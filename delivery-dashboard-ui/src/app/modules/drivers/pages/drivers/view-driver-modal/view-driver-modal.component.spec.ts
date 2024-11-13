import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewDriverModalComponent } from './view-driver-modal.component';

describe('ViewDriverModalComponent', () => {
  let component: ViewDriverModalComponent;
  let fixture: ComponentFixture<ViewDriverModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewDriverModalComponent]
    });
    fixture = TestBed.createComponent(ViewDriverModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
