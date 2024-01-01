import { Component, ElementRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { IDepartment } from "src/app/components/department/model/department.model";
import { DepartmentService } from "../service/department.service";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";

@Component({
  selector: "app-department-list",
  templateUrl: "./department-list.component.html",
  styleUrls: ["./department-list.component.scss"],
  providers: [MessageService],
})
export class DepartmentListComponent {
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  deleteDialog: any;
  departments?: IDepartment[];
  data: any = {};
  deleteId: any;

  constructor(
    private departmentService: DepartmentService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getDepartments();
    this.getAllProductFields();
  }

  getDepartments() {
    const params = { status: this.activeStatus };
    this.departmentService.getDepartments(params).subscribe((res) => {
      if (res && res.body) {
        this.departments = res.body;
      }
    });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getDepartments();
    } else {
      this.activeStatus = false;
      this.getDepartments();
    }
  }

  //   Get All Product Fields
  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productField = res;
      this.status = this.dropdownService.extractNames(
        this.productField.filter((data) => data.name == "Status")[0]
          .productFieldValuesList
      );
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
      if (res.status == 200) {
        this.alert();
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
    this.router.navigate(["create-department"], { queryParams: queryParams });
  }

  onActiveDepartment(id) {
    this.departmentService.updateDepartmentStatus(id).subscribe((res) => {
      this.success();
      this.selectedStatus = "Active";
      this.onStatusChange(this.selectedStatus);
    });
  }

  success() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Activation Successfull",
    });
  }

  alert() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Deactivation Successfull",
    });
  }
}
