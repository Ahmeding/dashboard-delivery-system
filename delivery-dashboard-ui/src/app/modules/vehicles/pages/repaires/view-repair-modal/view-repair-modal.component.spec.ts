import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewRepairModalComponent } from './view-repair-modal.component';

describe('ViewRepairModalComponent', () => {
  let component: ViewRepairModalComponent;
  let fixture: ComponentFixture<ViewRepairModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewRepairModalComponent]
    });
    fixture = TestBed.createComponent(ViewRepairModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
