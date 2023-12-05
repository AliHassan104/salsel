import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Table } from 'primeng/table';
import { IDepartment } from 'src/app/api/department.model';
import { DepartmentService } from '../service/department.service';

@Component({
  selector: 'app-department-list',
  templateUrl: './department-list.component.html',
  styleUrls: ['./department-list.component.scss']
})
export class DepartmentListComponent {
  deleteDialog: any;
  departments?: IDepartment[];
  data: any = {};
  deleteId: any;

  constructor(
    private departmentService: DepartmentService,
    private router: Router
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getDepartments();
  }

  getDepartments() {
    this.departmentService.getDepartments().subscribe((res) => {
      if(res && res.body){
        this.departments = res.body;
        console.log(this.departments)
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
    this.departmentService.removeDepartment(this.deleteId).subscribe((res) => {
      if(res.status == 200){
        this.getDepartments();
        this.deleteDialog = false;
      }
    });
  }


  deleteDepartment(id?: any) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  editDepartment(id?: any) {
    const queryParams = { id: id };
    this.router.navigate(["department/form"], {queryParams: queryParams});
  }
}
