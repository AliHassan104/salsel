import { DatePipe } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { finalize } from 'rxjs';
import { DropdownService } from 'src/app/layout/service/dropdown.service';
import { AccountService } from '../../accounts/service/account.service';
import { SessionStorageService } from '../../auth/service/session-storage.service';
import { HrModuleService } from '../service/hr-module.service';
import { IEmployee } from '../model/employeeDto';
import { FormvalidationService } from '../../Tickets/service/formvalidation.service';
import { TicktingService } from '../../Tickets/service/tickting.service';

@Component({
  selector: "app-hr-module-data",
  templateUrl: "./hr-module-data.component.html",
  styleUrls: ["./hr-module-data.component.scss"],
  providers: [MessageService, DatePipe],
})
export class HrModuleDataComponent {
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
    private employeeService: HrModuleService,
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
  employees?: IEmployee;
  deleteId: any;
  page?: any = 0;
  size?: number = 1;
  totalRecords?: number;

  ngOnInit(): void {
    // this.excelDataForm = new FormGroup({
    //   toDate: new FormControl(null, Validators.required),
    //   fromDate: new FormControl(null, Validators.required),
    // });

    this.getEmployees();
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
  getEmployees() {
    const queryParams = {
      status: this.activeStatus,
    };

    if (
      this.sessionStorageService.getRoleName() == "ADMIN" ||
      this.sessionStorageService.getRoleName() == "CUSTOMER_SERVICE_AGENT"
    ) {
      this.employeeService
        .getAllEmployees(queryParams)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          if (res.status == 200) {
            this.employees = res.body;
          }
        });
    } else {
      this.employeeService
        .getAllEmployees(queryParams)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          if (res.status == 200) {
            this.employees = res.body;
          }
        });
    }
  }

  onRefresh() {
    this.refresh = true;
    this.getEmployees();
  }

  onPageChange(event?: any) {
    this.page = event.page;
    this.getEmployees();
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
      this.getEmployees();
    } else {
      this.activeStatus = false;
      this.getEmployees();
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
    this.employeeService.removeEmployee(this.deleteId).subscribe((res) => {
      this.alert();
      this.getEmployees();
      this.deleteProductsDialog = false;
    });
  }

  //   onDownloadExcel(data: any) {
  //     if (this.excelDataForm.valid) {
  //       const formattedDates = {
  //         startDate: this.datePipe.transform(data.fromDate, "yyyy-MM-dd"),
  //         endDate: this.datePipe.transform(data.toDate, "yyyy-MM-dd"),
  //       };
  //       console.log(formattedDates);

  //       this._ticktingService.downloadEmployeeDataInExcel(formattedDates).subscribe(
  //         (res: any) => {
  //           this._ticktingService.downloadExcelFile(
  //             res,
  //             `Employee${formattedDates.startDate}_to_${formattedDates.endDate}.xlsx`
  //           );
  //           this.messageService.add({
  //             severity: "success",
  //             summary: "Success",
  //             detail: "Download Successfull",
  //           });
  //           this.excelDataForm.reset();
  //           this.visible = false;
  //         },
  //         (error) => {
  //           this.messageService.add({
  //             severity: "error",
  //             summary: "Error",
  //             detail: "No Data Found",
  //           });
  //         }
  //       );
  //     } else {
  //       this.formService.markFormGroupTouched(this.excelDataForm);
  //       this.messageService.add({
  //         severity: "error",
  //         summary: "Error",
  //         detail: "Please Fill All The Fields.",
  //       });
  //     }
  //   }

  onCancel() {
    this.visible = false;
  }

  //   Delete Employee

  onDeleteEmployee(id) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  //   Edit Employee
  onEditEmployee(id) {
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["create-employee"], {
      queryParams: queryParams,
    });
  }

  onDownloadEmployeeInfo(id, empNum) {
    this.employeeService.getEmployeeForm(id).subscribe(
      (res: any) => {
        this.downloadSuccess();
        this.employeeService.downloadFile(res, `Employee_${empNum}.pdf`);
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
    this.employeeService.updateEmployeeStatus(id).subscribe((res) => {
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
