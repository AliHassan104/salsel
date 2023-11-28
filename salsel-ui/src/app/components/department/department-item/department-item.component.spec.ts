import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartmentItemComponent } from './department-item.component';

describe('DepartmentItemComponent', () => {
  let component: DepartmentItemComponent;
  let fixture: ComponentFixture<DepartmentItemComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepartmentItemComponent]
    });
    fixture = TestBed.createComponent(DepartmentItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
