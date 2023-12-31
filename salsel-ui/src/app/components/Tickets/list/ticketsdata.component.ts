import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";
import { Table } from "primeng/table";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { Ticket } from "src/app/components/Tickets/model/ticketValuesDto";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { AccountService } from "../../accounts/service/account.service";

@Component({
  selector: "app-ticketsdata",
  templateUrl: "./ticketsdata.component.html",
  styleUrls: ["./ticketsdata.component.scss"],
  providers: [MessageService],
})
export class TicketsdataComponent implements OnInit {
  //   Activity Work
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  deleteProductsDialog: any;
  serachText?: string;
  constructor(
    private _ticktingService: TicktingService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService,
    private accountService: AccountService
  ) {}

  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  tickets?: Ticket;
  deleteId: any;
  page?: any = 0;
  size?: number = 1;
  totalRecords?: number;

  ngOnInit(): void {
    this.getTickets();
    this.getAllProductFields();
  }

  //   Get all tickets
  getTickets() {
    const queryParams = {
      status: this.activeStatus,
    };

    this._ticktingService.getTickets(queryParams).subscribe((res: any) => {
      if (res.status == 200) {
        this.tickets = res.body;
      }
    });
  }

  onPageChange(event?: any) {
    this.page = event.page;
    this.getTickets();
  }

  //   For table filtering purpose
  onGlobalFilter(table: Table, event: any) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = "";
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getTickets();
    } else {
      this.activeStatus = false;
      this.getTickets();
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

  confirmDeleteSelected() {
    this._ticktingService.deleteTicket(this.deleteId).subscribe((res) => {
      this.alert();
      this.getTickets();
      this.deleteProductsDialog = false;
    });
  }

  onDownloadAttachment(url, id) {
    this.accountService.downloadAgreement(url).subscribe(
      (res: any) => {
        this.downloadSuccess();
        this.accountService.downloadFile(res, `Ticket_Attachment_${id}`);
      },
      (error) => {
        this.downloadError();
      }
    );
  }

  downloadError() {
    this.messageService.add({
      severity: "error",
      summary: "Error",
      detail: "Attachment Not Found",
    });
  }

  downloadSuccess() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "File Successfully Downloaded",
    });
  }

  //   Delete Ticket

  onDeleteTicket(id) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  //   Edit Ticket
  onEditTicket(id) {
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["create-ticket"], {
      queryParams: queryParams,
    });
  }

  onActiveTicket(id) {
    this._ticktingService.updateTicketStatus(id).subscribe((res) => {
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

  getPriorityClass(value: any): string {
    switch (value) {
      case "Normal":
        return "primary";
      case "Urgent":
        return "warning";
      case "Priority":
        return "success";
      case "Extreme Urgent":
        return "danger";
      default:
        return "";
    }
  }

  getStatusClass(value: any): string {
    switch (value) {
      case "Open":
        return "primary";
      case "Closed":
        return "danger";
      case "On-Hold":
        return "warning";
      case "Under Process":
        return "success";
      case "Overdue Escalation":
        return "warning";
      case "Held-FI":
        return "success";
      default:
        return "";
    }
  }
}
