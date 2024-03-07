import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { AirbillService } from "../service/airbill.service";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { IAwbDto } from "src/app/components/awb/model/awbValuesDto";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { finalize } from "rxjs";
import { RolesService } from "../../permissions/service/roles.service";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { DatePipe } from "@angular/common";
import {
  NgxScannerQrcodeComponent,
  NgxScannerQrcodeService,
  ScannerQRCodeConfig,
  ScannerQRCodeResult,
  ScannerQRCodeSelectedFiles,
} from "ngx-scanner-qrcode";

@Component({
  selector: "app-airbilldata",
  templateUrl: "./airbilldata.component.html",
  styleUrls: ["./airbilldata.component.scss"],
  providers: [MessageService, DatePipe],
})
export class AirbilldataComponent implements OnInit {
  public config: ScannerQRCodeConfig = {
    constraints: {
      video: {
        width: window.innerWidth,
      },
    },
  };

  @ViewChild("action") action!: NgxScannerQrcodeComponent;

  codeScan: boolean = false;
  scanning: boolean = true;
  uniqueScanNum;
  scanOptions;

  excelDataForm!: FormGroup;
  minDate;
  maxDate;
  visible;
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  deleteProductsDialog: any;
  assignDialog: boolean = false;
  refresh: boolean = true;
  userRole?;
  assignedTo: any;
  assignId: any;
  constructor(
    private _airbillService: AirbillService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService,
    private roleService: RolesService,
    private formService: FormvalidationService,
    private datePipe: DatePipe,
    private qrcode: NgxScannerQrcodeService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  bills: IAwbDto;
  deleteId: any;
  awbForm!: FormGroup;
  scanForm!: FormGroup;

  ngOnInit(): void {
    this.excelDataForm = new FormGroup({
      toDate: new FormControl(null, Validators.required),
      fromDate: new FormControl(null, Validators.required),
    });
    this.awbForm = new FormGroup({
      assignedTo: new FormControl(null, [Validators.required]),
    });
    this.scanForm = new FormGroup({
      updatedStatus: new FormControl(null, [Validators.required]),
    });

    this.getAirbills();
    this.getAllProductFields();
    this.getMinMax();
  }

  onUpdateStatus(data: any) {
    if (this.scanForm.valid) {
      this._airbillService
        .updateAwbTrackingStatus(data?.updatedStatus, this.uniqueScanNum)
        .subscribe((res) => {
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Status Updated",
          });
          this.scanForm.reset();
          this.codeScan = false;
          this.scanning = true;
          this.getAirbills();
        });
    } else {
      this.formService.markFormGroupTouched(this.scanForm);
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Please ensure that required Field is filled out.",
      });
    }
  }

  public onEvent(e: ScannerQRCodeResult[], action?: any): void {
    e && action && action.stop();
    this.scanning = false;
    this.uniqueScanNum = e[0]?.value;
    console.log(e[0]?.value);
  }

  handle(action: any): void {
    this.codeScan = true;
    action.start();
  }

  onCloseScan() {
    this.scanning = true;
    this.action?.stop();
  }

  getMinMax() {
    this._airbillService.getMinMax().subscribe((res: any) => {
      this.minDate = new Date(res.minDate);
      this.maxDate = new Date(res.maxDate);
    });
  }

  getAirbills() {
    const params = { status: this.activeStatus };

    if (
      this.sessionStorageService.getRoleName() == "ADMIN" ||
      this.sessionStorageService.getRoleName() == "CUSTOMER_SERVICE_AGENT"
    ) {
      this._airbillService
        .getBills(params)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          this.bills = res;
        });
    } else {
      this._airbillService
        .getBillsByUserEmailAndRole(params)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          this.bills = res;
        });
    }
  }

  onDownloadExcel(data: any) {
    if (this.excelDataForm.valid) {
      const formattedDates = {
        startDate: this.datePipe.transform(data.fromDate, "yyyy-MM-dd"),
        endDate: this.datePipe.transform(data.toDate, "yyyy-MM-dd"),
      };
      console.log(formattedDates);

      this._airbillService.downloadAwbDataInExcel(formattedDates).subscribe(
        (res: any) => {
          this._airbillService.downloadExcelFile(
            res,
            `Awb_${formattedDates.startDate}_to_${formattedDates.endDate}.xlsx`
          );
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Download Successfull",
          }),
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

  onRefresh() {
    this.refresh = true;
    this.getAirbills();
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getAirbills();
    } else {
      this.activeStatus = false;
      this.getAirbills();
    }
  }

  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productField = res;
      this.status = this.dropdownService.extractNames(
        this.productField.filter((data) => data?.name == "Status")[0]
          .productFieldValuesList
      );
      this.scanOptions = this.dropdownService.extractNames(
        this.productField.filter((data) => data?.name == "Awb Status")[0]
          .productFieldValuesList
      );
    });

    // Get All Roles
    this.roleService.getRoles().subscribe((res: any) => {
      this.assignedTo = res;
      this.assignedTo = this.dropdownService.extractNames(this.assignedTo);
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
    this._airbillService.deleteBill(this.deleteId).subscribe((res) => {
      this.alert();
      this.getAirbills();
      this.deleteProductsDialog = false;
    });
  }

  //   Delete Ticket

  onDeleteBill(id) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  //   Edit Bill
  onEditBill(id) {
    this._airbillService.updateAWB.next(true);
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["create-awb"], {
      queryParams: queryParams,
    });
  }

  onDownloadBill(id, num) {
    this._airbillService.downloadBill(id).subscribe(
      (res: any) => {
        this.downloadSuccess();
        this._airbillService.downloadFile(res, `Awb_${num}.pdf`);
      },
      (error) => {
        this.downloadError();
      }
    );
  }

  onActiveBill(id) {
    this._airbillService.updateBillStatus(id).subscribe((res: any) => {
      this.success();
      this.selectedStatus = "Active";
      this.onStatusChange(this.selectedStatus);
    });
  }

  onViewHistory(id) {}

  onAssignBill(id) {
    this.assignDialog = true;
    this.assignId = id;
  }

  onCloseDialog() {
    this.awbForm.reset();
  }

  onSubmit(data: any) {
    if (this.awbForm.valid) {
      this._airbillService
        .updateAssignedTo(this.assignId, data)
        .subscribe((res: any) => {
          this.assignDialog = false;
          this.awbForm.reset();
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Successfully Assigned",
          });
        });
    } else {
      this.formService.markFormGroupTouched(this.awbForm);
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Please ensure that required Field is filled out.",
      });
    }
  }

  success() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Activation Successfull",
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

  alert() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Deactivation Successfull",
    });
  }
}
