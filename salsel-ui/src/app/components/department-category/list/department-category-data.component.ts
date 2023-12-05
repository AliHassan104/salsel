import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Table } from 'primeng/table';
import { IDepartment } from 'src/app/api/department.model';
import { DepartmentService } from '../../department/service/department.service';
import { IDepartmentCategory } from 'src/app/api/department-category.model';
import { DepartmentCategoryService } from '../service/department-category.service';

@Component({
  selector: 'app-department-category-data',
  templateUrl: './department-category-data.component.html',
  styleUrls: ['./department-category-data.component.scss']
})
export class DepartmentCategoryDataComponent {
  deleteDialog: any;
  departmentCategories?: IDepartmentCategory[];
  deleteId: any;

  constructor(
    private departmentCategoryService: DepartmentCategoryService,
    private router: Router
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getDepartmentCategories();
  }

  getDepartmentCategories() {
    this.departmentCategoryService.getDepartmentCategories().subscribe((res) => {
      if(res && res.body){
        this.departmentCategories = res.body;
        console.log(this.departmentCategories)
      }
    });
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = "";
  }
  confirmDeleteSelected() {
    this.departmentCategoryService.removeDepartmentCategory(this.deleteId).subscribe((res) => {
      if(res.status == 200){
        this.getDepartmentCategories();
        this.deleteDialog = false;
      }
    });
  }


  deleteDepartmentCategory(id?: any) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  editDepartmentCategory(id?: any) {
    const queryParams = { id: id };
    this.router.navigate(["department-category/form"], {queryParams: queryParams});
  }
}
