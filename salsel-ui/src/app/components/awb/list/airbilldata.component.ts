import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from "@angular/core";
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
import { UserService } from "../../auth/usermanagement/service/user.service";
import { AccountService } from "../../accounts/service/account.service";
import { Dropdown } from "primeng/dropdown";

@Component({
  selector: "app-airbilldata",
  templateUrl: "./airbilldata.component.html",
  styleUrls: ["./airbilldata.component.scss"],
  providers: [MessageService, DatePipe],
})
export class AirbilldataComponent implements OnInit {
  users: any;
  employeeId: any;
  excelDataForm!: FormGroup;
  minDate;
  maxDate;
  visible;
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;
  accountNumbers: any;
  trackingNumbers: any = [];

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
    private userService: UserService,
    private accountService: AccountService
  ) {}

  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  bills: IAwbDto;
  deleteId: any;
  awbForm!: FormGroup;
  scanForm!: FormGroup;

  @ViewChild("dropdown10") dropdown?: Dropdown;

  ngOnInit(): void {
    this.excelDataForm = new FormGroup({
      toDate: new FormControl(null),
      fromDate: new FormControl(null),
      accountNumbers: new FormControl(null),
      dataType: new FormControl(null, Validators.required),
    });
    this.awbForm = new FormGroup({
      assignedTo: new FormControl(null, [Validators.required]),
      employeeId: new FormControl(null, [Validators.required]),
      name: new FormControl(null, [Validators.required]),
    });

    this.getAirbills();
    this.getAllProductFields();
    this.getMinMax();
    this.getUsers();
    this.getAllAccountNumbers();
  }

  onShowModal() {
    this.awbForm.patchValue({
      assignedTo: "ROLE_COURIER",
    });
  }

  onCloseExcelDialog(){

    this.excelDataForm.reset();

  }

  toggleValidators(value: string): void {
    const toDateControl = this.excelDataForm.get("toDate");
    const fromDateControl = this.excelDataForm.get("fromDate");
    const accountNumbersControl = this.excelDataForm.get("accountNumbers");
    toDateControl.clearValidators();
    fromDateControl.clearValidators();
    accountNumbersControl.clearValidators();
    toDateControl.reset();
    fromDateControl.reset();
    accountNumbersControl.reset();

    if (value === "Date") {
      toDateControl.setValidators([Validators.required]);
      fromDateControl.setValidators([Validators.required]);
    } else if (value === "Account Number") {
      accountNumbersControl.setValidators([Validators.required]);
    } else {
      // Clear all validators if "All" is selected
      toDateControl.clearValidators();
      fromDateControl.clearValidators();
      accountNumbersControl.clearValidators();
      toDateControl.reset();
      fromDateControl.reset();
      accountNumbersControl.reset();
    }

    // Trigger validation after updating validators
    toDateControl.updateValueAndValidity();
    fromDateControl.updateValueAndValidity();
    accountNumbersControl.updateValueAndValidity();
  }

  getAllAccountNumbers() {
    this.accountService
      .getAllAccountNumbers({ status: true })
      .subscribe((res: any) => {
        this.accountNumbers = res.body;

        // this.preprocessedAccountNumbers = this.accountNumbers.map(
        //   (account: any) => ({
        //     label: `${account.accountNumber}, ${account.customerName}`,
        //     value: account?.accountNumber,
        //   })
        // );
      });
  }

  getMinMax() {
    this._airbillService.getMinMax().subscribe((res: any) => {
      this.minDate = new Date(res.minDate);
      this.maxDate = new Date(res.maxDate);
    });
  }

  getUsers() {
    this.userService.getUserByRoleName("ROLE_COURIER").subscribe((res: any) => {
      this.users = res;
      this.employeeId = res;
    });
  }

  getUserByRoleName(data: any) {
    this.userService.getUserByRoleName(data?.value).subscribe((res: any) => {
      this.users = res;
      this.employeeId = res;
    });
  }

  onSelectName(data: any) {
    let user = this.users.filter((x) => x?.name == data?.value?.name);

    if (user.length > 1) {
      this.employeeId = user;
    } else {
      this.employeeId = this.users;
      this.awbForm.patchValue({
        employeeId: user[0],
      });
    }
  }

  onSelectEmployeeId(data: any) {
    let user = this.users.filter(
      (x) => x?.employeeId == data?.value?.employeeId
    );

    if (user) {
      this.awbForm.patchValue({
        name: user[0],
      });
    }
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

  excelDataDownload(startDate, endDate, accountNumbers, all, Name: string) {
    this._airbillService
      .downloadAwbDataInExcel(startDate, endDate, accountNumbers, all)
      .subscribe(
        (res: any) => {
          this._airbillService.downloadExcelFile(res, Name);
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
            detail: "No Airbill found",
          });
        }
      );
  }

  onSelectAccountNumbers(value: any) {
    const selectedValues = value?.value;
    const trackingField = this.excelDataForm.get("accountNumbers");
    const currentValue = trackingField?.value || "";
    const newValues = selectedValues.join("\n");
    trackingField?.setValue(newValues);
  }

  validateTextarea(event: any) {
    const pattern = /^[0-9\n]*$/;
    const inputValue = event.target.value;
    if (!pattern.test(inputValue)) {
      event.target.value = inputValue.replace(/[^0-9\n]/g, "");
    }
  }

  onDownloadExcel(data: any) {
    if (this.excelDataForm.valid) {
      const dataType = this.excelDataForm.get("dataType")?.value;
      if (dataType == "Account Number") {
        const values = data?.accountNumbers
          .split(/[\n\s,]+/) // Split on newlines, spaces, and commas
          .map((value) => value.trim()) // Trim whitespace
          .filter((value) => value); // Filter out empty strings

        this.trackingNumbers = values;

        this.excelDataDownload(
          null,
          null,
          this.trackingNumbers,
          null,
          `Awb_by_accountNumbers.xlsx`
        );
      } else if (dataType == "Date") {
        const formattedDates = {
          startDate: this.datePipe.transform(data.fromDate, "yyyy-MM-dd"),
          endDate: this.datePipe.transform(data.toDate, "yyyy-MM-dd"),
        };

        this.excelDataDownload(
          formattedDates.startDate,
          formattedDates.endDate,
          null,
          null,
          `Awb_${formattedDates.startDate}_to_${formattedDates.endDate}.xlsx`
        );
      } else {
        this.excelDataDownload(null, null, null, true, `All_Awb.xlsx`);
      }
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
        .assignedAirbillToUser(this.assignId, data?.employeeId?.id)
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
