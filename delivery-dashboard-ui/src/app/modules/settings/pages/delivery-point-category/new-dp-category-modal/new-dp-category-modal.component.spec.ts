import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDpCategoryModalComponent } from './new-dp-category-modal.component';

describe('NewDpCategoryModalComponent', () => {
  let component: NewDpCategoryModalComponent;
  let fixture: ComponentFixture<NewDpCategoryModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewDpCategoryModalComponent]
    });
    fixture = TestBed.createComponent(NewDpCategoryModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
