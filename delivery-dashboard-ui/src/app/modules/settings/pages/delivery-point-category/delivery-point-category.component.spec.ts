import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveryPointCategoryComponent } from './delivery-point-category.component';

describe('DeliveryPointCategoryComponent', () => {
  let component: DeliveryPointCategoryComponent;
  let fixture: ComponentFixture<DeliveryPointCategoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeliveryPointCategoryComponent]
    });
    fixture = TestBed.createComponent(DeliveryPointCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
