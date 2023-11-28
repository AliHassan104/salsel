import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Table } from 'primeng/table';
import { DepartmentService } from 'src/app/service/department.service';

@Component({
  selector: 'app-department-data',
  templateUrl: './department-data.component.html',
  styleUrls: ['./department-data.component.scss']
})
export class DepartmentDataComponent {
  deleteProductsDialog: any;
  constructor(
    private departmentService: DepartmentService,
    private router: Router
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  departments : []
  data: any = {};
  deleteId: any;

  ngOnInit(): void {
    this.getDepartment();
  }

  //   Get all tickets
  getDepartment() {
    this.departmentService.getAll().subscribe((res) => {
      this.data = res;
      this.departments = this.data.notes;
    });
  }

  //   For table filtering purpose
  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = "";
  }
  confirmDeleteSelected() {
    this.departmentService.delete(this.deleteId).subscribe((res) => {
      this.getDepartment();
      this.deleteProductsDialog = false;
    });
  }

  //   Delete Ticket

  onDeleteDepartment(id) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  //   Edit Ticket
  onEditDepartment(id) {
    console.log(id);
    this.departmentService.editId.next(id);
    this.router.navigate(["addticket"]);
    this.departmentService.editTicketMode.next(true);
  }
}
