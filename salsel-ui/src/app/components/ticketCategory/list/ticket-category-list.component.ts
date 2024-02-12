import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { DropdownService } from 'src/app/layout/service/dropdown.service';
import { SessionStorageService } from '../../auth/service/session-storage.service';
import { IDepartmentCategory } from '../../department-category/model/department-category.model';
import { DepartmentCategoryService } from '../../department-category/service/department-category.service';
import { TicketCategoryService } from '../service/ticket-category.service';
import { ITicketCategory } from '../model/ticketCategoryDto';

@Component({
  selector: "app-ticket-category-list",
  templateUrl: "./ticket-category-list.component.html",
  styleUrls: ["./ticket-category-list.component.scss"],
  providers:[MessageService]
})
export class TicketCategoryListComponent {
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  deleteDialog: any;
  ticketCategories?: ITicketCategory[];
  deleteId: any;

  constructor(
    private departmentCategoryService: DepartmentCategoryService,
    private ticketCategoryService : TicketCategoryService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService :SessionStorageService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getTicketCategories();
    this.getAllProductFields();
  }

  getTicketCategories() {
    const params = { status: this.activeStatus };
    this.ticketCategoryService
      .getTicketCategories(params)
      .subscribe((res) => {
        if (res && res.body) {
          this.ticketCategories = res.body;
        }
      });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getTicketCategories();
    } else {
      this.activeStatus = false;
      this.getTicketCategories();
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
    this.ticketCategoryService
      .removeTicketCategory(this.deleteId)
      .subscribe((res) => {
        if (res.status == 200) {
          this.getTicketCategories();
          this.deleteDialog = false;
        }
      });
  }

  deleteTicketCategory(id?: any) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  editTicketCategory(id?: any) {
    const queryParams = { id: id };
    this.router.navigate(["create-ticket-category"], {
      queryParams: queryParams,
    });
  }

  onActiveTicketCategory(id) {
    this.ticketCategoryService
      .updateTicketCategoryStatus(id)
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
