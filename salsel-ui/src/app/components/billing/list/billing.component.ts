import { DatePipe } from "@angular/common";
import { Component, ElementRef, ViewChild } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { MessageService } from "primeng/api";
import { Table } from "primeng/table";
import { catchError, finalize, of, tap } from "rxjs";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { IBilling } from "../model/billingDto";
import { BillingService } from "../service/billing.service";
import * as XLSX from "xlsx";

@Component({
  selector: "app-billing",
  templateUrl: "./billing.component.html",
  styleUrls: ["./billing.component.scss"],
  providers: [MessageService, DatePipe],
})
export class BillingComponent {
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
  fileName;
  uploadedSheet: File;
  fileInput;
  excelViewDialog: boolean = false;
  excelUrl;
  excelData;
  tableHeaders;

  constructor(
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService,
    private billingService: BillingService
  ) {}

  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  invoices?: IBilling;
  deleteId: any;
  page?: any = 0;
  size?: number = 1;
  totalRecords?: number;

  ngOnInit(): void {
    // this.excelDataForm = new FormGroup({
    //   toDate: new FormControl(null, Validators.required),
    //   fromDate: new FormControl(null, Validators.required),
    // });

    this.getInvoices();
    this.getAllProductFields();
    // this.getMinMax();
  }

  //   getMinMax() {
  //     this._ticktingService.getMinMax().subscribe((res: any) => {
  //       this.minDate = new Date(res.minDate);
  //       this.maxDate = new Date(res.maxDate);
  //     });
  //   }

  //   Get all Employees
  getInvoices() {
    const queryParams = {
      status: this.activeStatus,
    };

    if (
      this.sessionStorageService.getRoleName() == "ADMIN" ||
      this.sessionStorageService.getRoleName() == "CUSTOMER_SERVICE_AGENT"
    ) {
      this.billingService
        .getAllInvoices(queryParams)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          if (res.status == 200) {
            this.invoices = res.body;
            console.log(this.invoices);
          }
        });
    } else {
      this.billingService
        .getAllInvoices(queryParams)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          if (res.status == 200) {
            this.invoices = res.body;
            console.log(this.invoices);
          }
        });
    }
  }

  onFileSelected(event: any) {
    this.fileInput = null;
    this.fileInput = event.target;
    if (this.fileInput.files && this.fileInput.files.length > 0) {
      const selectedFile = this.fileInput.files[0];

      // Reset the input value to ensure the change event is triggered even if the same file is selected again
      event.target.value = "";

      if (selectedFile.name.toLowerCase().endsWith(".xlsx")) {
        this.fileName = selectedFile.name;
        this.uploadedSheet = selectedFile;
        this.openExcelDialog();
      } else {
        this.fileInput.value = null;
        this.fileName = null;
        this.uploadedSheet = null;
        this.messageService.add({
          severity: "error",
          summary: "Invalid File",
          detail: "Please select a valid XLSX file.",
        });
      }
    }
  }

  //   uploadSheet() {
  //     this.billingService
  //       .uploadExcelFileToGetData(this.uploadedSheet)
  //       .subscribe((res: any) => {
  //       });
  //   }

  uploadSheet() {
    this.billingService.uploadExcelFileToGetData(this.uploadedSheet).subscribe(
      (res: any) => {
        this.visible = false;
        this.uploadedSheet = null;
        this.getInvoices();
      },
      (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: error?.error?.error,
        });
      }
    );
  }

  openExcelDialog() {
    if (this.uploadedSheet) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: "array" });

        // Extract data from the first sheet of the workbook
        const firstSheetName = workbook.SheetNames[0];
        const worksheet = workbook.Sheets[firstSheetName];
        this.excelData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

        // Extract table headers
        this.tableHeaders = this.excelData.shift(); // Remove the first row (headers)
      };
      reader.readAsArrayBuffer(this.uploadedSheet);
    } else {
      this.messageService.add({
        severity: "error",
        summary: "No File Selected",
        detail: "Please select an Excel file before viewing.",
      });
    }
  }

  viewExcelFile() {
    this.excelViewDialog = true;
  }

  clearFileInput() {
    this.fileInput = null;
    this.fileName = null;
    this.uploadedSheet = null;

    // Manually trigger the change event
    const inputChangeEvent = new Event("change", { bubbles: true });
    this.fileInput?.dispatchEvent(inputChangeEvent);
  }

  getExcelFormatSheet() {
    this.billingService.downloadUploadExcelFormat().subscribe((res: any) => {
      this.billingService.downloadExcelFile(res.body, "UploadInvoice.xlsx");
    });
  }

  getBillingStatement() {
    this.billingService.downloadStatementExcelFormat().subscribe((res: any) => {
      this.billingService.downloadExcelFile(res.body, "Billing_Statement.xlsx");
    });
  }

  onResendInvoice(id: any) {
    this.billingService.resendInvoice(id).subscribe(
      (res: any) => {
        this.messageService.add({
          severity: "success",
          summary: "Success",
          detail: "Email Sent Successfully.",
        });
      },
      (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: error?.error?.error,
        });
      }
    );
  }

  onCloseDialog() {
    this.clearFileInput();
  }

  onRefresh() {
    this.refresh = true;
    this.getInvoices();
  }

  onPageChange(event?: any) {
    this.page = event.page;
    this.getInvoices();
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
      this.getInvoices();
    } else {
      this.activeStatus = false;
      this.getInvoices();
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
    this.billingService.removeInvoice(this.deleteId).subscribe((res) => {
      this.alert();
      this.getInvoices();
      this.deleteProductsDialog = false;
    });
  }

  onCancel() {
    this.visible = false;
  }

  //   Delete Employee

  onDeleteEmployee(id) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  onDownlaodBill() {
    this.billingService.getBillingReports().subscribe((res: any) => {
      this.billingService.downloadFile(res, "bill.pdf");
      this.billingService.getBillingReportsxl().subscribe((res: any) => {
        this.billingService.downloadExcelFile(res, "bill.xlsx");
      });
    });
  }

  onDownlaodBillingStatement(id: any) {
    this.billingService.getBillingStatement(id).subscribe((res: any) => {
      this.billingService.downloadExcelFile(res, "billing_statement.xlsx");
    });
  }

  onDownlaodBillingInvoice(id: any) {
    this.billingService.getBillingInvoice(id).subscribe((res: any) => {
      this.billingService.downloadFile(res, "billing.pdf");
    });
  }

  downloadError() {
    this.messageService.add({
      severity: "error",
      summary: "Error",
      detail: "Download Failed",
    });
  }

  downloadSuccess() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "File Successfully Downloaded",
    });
  }

  onActiveEmployee(id) {
    this.billingService.updateInvocieStatus(id).subscribe((res) => {
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
