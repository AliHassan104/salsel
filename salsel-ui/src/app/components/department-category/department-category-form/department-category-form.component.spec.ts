import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartmentCategoryFormComponent } from './department-category-form.component';

describe('DepartmentCategoryFormComponent', () => {
  let component: DepartmentCategoryFormComponent;
  let fixture: ComponentFixture<DepartmentCategoryFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepartmentCategoryFormComponent]
    });
    fixture = TestBed.createComponent(DepartmentCategoryFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
