import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartmentCategoryDataComponent } from './department-category-data.component';

describe('DepartmentCategoryDataComponent', () => {
  let component: DepartmentCategoryDataComponent;
  let fixture: ComponentFixture<DepartmentCategoryDataComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepartmentCategoryDataComponent]
    });
    fixture = TestBed.createComponent(DepartmentCategoryDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
