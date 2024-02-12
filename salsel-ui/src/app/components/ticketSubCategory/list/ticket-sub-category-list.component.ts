import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { DropdownService } from 'src/app/layout/service/dropdown.service';
import { SessionStorageService } from '../../auth/service/session-storage.service';
import { DepartmentCategoryService } from '../../department-category/service/department-category.service';
import { ITicketCategory } from '../../ticketCategory/model/ticketCategoryDto';
import { TicketCategoryService } from '../../ticketCategory/service/ticket-category.service';
import { ITicketSubCategory } from '../model/ticketSubCategoryDeto';
import { TicketSubCategoryService } from '../service/ticket-sub-category.service';

@Component({
  selector: "app-ticket-sub-category-list",
  templateUrl: "./ticket-sub-category-list.component.html",
  styleUrls: ["./ticket-sub-category-list.component.scss"],
  providers: [MessageService],
})
export class TicketSubCategoryListComponent {
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  deleteDialog: any;
  ticketSubCategories?: ITicketSubCategory[];
  deleteId: any;

  constructor(
    private departmentCategoryService: DepartmentCategoryService,
    private ticketCategoryService: TicketCategoryService,
    private ticketSubCategoryService: TicketSubCategoryService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getticketSubCategories();
    this.getAllProductFields();
  }

  getticketSubCategories() {
    const params = { status: this.activeStatus };
    this.ticketSubCategoryService
      .getTicketSubCategories(params)
      .subscribe((res) => {
        if (res && res.body) {
          this.ticketSubCategories = res.body;
        }
      });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getticketSubCategories();
    } else {
      this.activeStatus = false;
      this.getticketSubCategories();
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
    this.ticketSubCategoryService
      .removeTicketSubCategory(this.deleteId)
      .subscribe((res) => {
        if (res.status == 200) {
          this.getticketSubCategories();
          this.deleteDialog = false;
        }
      });
  }

  deleteTicketSubCategory(id?: any) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  editTicketSubCategory(id?: any) {
    const queryParams = { id: id };
    this.router.navigate(["create-ticket-sub-category"], {
      queryParams: queryParams,
    });
  }

  onActiveTicketSubCategory(id) {
    this.ticketSubCategoryService
      .updateTicketSubCategoryStatus(id)
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
