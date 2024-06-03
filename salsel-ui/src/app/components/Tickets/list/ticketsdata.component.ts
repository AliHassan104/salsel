import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";
import { Table } from "primeng/table";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { Ticket } from "src/app/components/Tickets/model/ticketValuesDto";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { AccountService } from "../../accounts/service/account.service";
import { finalize } from "rxjs";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { DatePipe } from "@angular/common";
import { FormvalidationService } from "../service/formvalidation.service";

@Component({
  selector: "app-ticketsdata",
  templateUrl: "./ticketsdata.component.html",
  styleUrls: ["./ticketsdata.component.scss"],
  providers: [MessageService, DatePipe],
})
export class TicketsdataComponent implements OnInit {
  //   Activity Work
  excelDataForm!: FormGroup;
  minDate;
  maxDate;
  visible;
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;
  deleteProductsDialog: any;
  serachText?: string;
  refresh: boolean = true;

  constructor(
    private _ticktingService: TicktingService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private datePipe: DatePipe,
    private formService: FormvalidationService
  ) {}

  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  tickets?: Ticket;
  deleteId: any;
  page?: any = 0;
  size?: number = 1;
  totalRecords?: number;

  ngOnInit(): void {
    this.excelDataForm = new FormGroup({
      toDate: new FormControl(null, Validators.required),
      fromDate: new FormControl(null, Validators.required),
    });

    this.getTickets();
    this.getAllProductFields();
    this.getMinMax();
  }

  getMinMax() {
    this._ticktingService.getMinMax().subscribe((res: any) => {
      this.minDate = new Date(res.minDate);
      this.maxDate = new Date(res.maxDate);
    });
  }

  //   Get all tickets
  getTickets() {
    const queryParams = {
      status: this.activeStatus,
    };

    if (
      this.sessionStorageService.getRoleName() == "ADMIN" ||
      this.sessionStorageService.getRoleName() == "CUSTOMER_SERVICE_AGENT"
    ) {
      this._ticktingService
        .getTickets(queryParams)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          if (res.status == 200) {
            this.tickets = res.body;

          }
        });
    } else {
      this._ticktingService
        .getTicketsByLoggedInUserAndRole(queryParams)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          if (res.status == 200) {
            this.tickets = res.body;
          }
        });
    }
  }

  onRefresh() {
    this.refresh = true;
    this.getTickets();
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

  onDownloadExcel(data: any) {
    if (this.excelDataForm.valid) {
      const formattedDates = {
        startDate: this.datePipe.transform(data.fromDate, "yyyy-MM-dd"),
        endDate: this.datePipe.transform(data.toDate, "yyyy-MM-dd"),
      };
      console.log(formattedDates);

      this._ticktingService.downloadTicketDataInExcel(formattedDates).subscribe(
        (res: any) => {
          this._ticktingService.downloadExcelFile(
            res,
            `Ticket${formattedDates.startDate}_to_${formattedDates.endDate}.xlsx`
          );
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Download Successfull",
          });
          this.excelDataForm.reset();
          this.visible = false;
        },
        (error) => {
          this.messageService.add({
            severity: "error",
            summary: "Error",
            detail: "No Data Found",
          });
        }
      );
    } else {
      this.formService.markFormGroupTouched(this.excelDataForm);
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Please Fill All The Fields.",
      });
    }
  }

  onCancel() {
    this.visible = false;
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
