import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewContractModalComponent } from './view-contract-modal.component';

describe('ViewContractModalComponent', () => {
  let component: ViewContractModalComponent;
  let fixture: ComponentFixture<ViewContractModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewContractModalComponent]
    });
    fixture = TestBed.createComponent(ViewContractModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
