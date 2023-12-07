import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartmentCategoryItemComponent } from './department-category-item.component';

describe('DepartmentCategoryItemComponent', () => {
  let component: DepartmentCategoryItemComponent;
  let fixture: ComponentFixture<DepartmentCategoryItemComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepartmentCategoryItemComponent]
    });
    fixture = TestBed.createComponent(DepartmentCategoryItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
