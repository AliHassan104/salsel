import { Component, ElementRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { IDepartmentCategory } from "src/app/components/department-category/model/department-category.model";
import { DepartmentCategoryService } from "../service/department-category.service";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";

@Component({
  selector: "app-department-category-data",
  templateUrl: "./department-category-data.component.html",
  styleUrls: ["./department-category-data.component.scss"],
  providers: [MessageService],
})
export class DepartmentCategoryDataComponent {
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  deleteDialog: any;
  departmentCategories?: IDepartmentCategory[];
  deleteId: any;

  constructor(
    private departmentCategoryService: DepartmentCategoryService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,

    public sessionStorageService: SessionStorageService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getDepartmentCategories();
    this.getAllProductFields();
  }

  getDepartmentCategories() {
    const params = { status: this.activeStatus };
    this.departmentCategoryService
      .getDepartmentCategories(params)
      .subscribe((res) => {
        if (res && res.body) {
          this.departmentCategories = res.body;
        }
      });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getDepartmentCategories();
    } else {
      this.activeStatus = false;
      this.getDepartmentCategories();
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
    this.departmentCategoryService
      .removeDepartmentCategory(this.deleteId)
      .subscribe((res) => {
        if (res.status == 200) {
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
    this.router.navigate(["create-department-category"], {
      queryParams: queryParams,
    });
  }

  onActiveDepartmentCategory(id) {
    this.departmentCategoryService
      .updateDepartmentCategoryStatus(id)
      .subscribe((res) => {
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
