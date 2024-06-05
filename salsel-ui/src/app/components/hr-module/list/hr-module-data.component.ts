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
import { DepartmentService } from '../../department/service/department.service';
import { CountryService } from '../../country/service/country.service';

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
  department?;
  countries?;
  isAnyFieldFilled: boolean = false;

  constructor(
    private _ticktingService: TicktingService,
    private employeeService: HrModuleService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private datePipe: DatePipe,
    private formService: FormvalidationService,
    private departmentService: DepartmentService,
    private countryService: CountryService
  ) {}

  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  employees?: IEmployee;
  deleteId: any;
  page?: any = 0;
  size?: number = 1;
  totalRecords?: number;

  ngOnInit(): void {
    this.excelDataForm = new FormGroup({
      department: new FormControl(null),
      country: new FormControl(null),
    });

    this.getEmployees();
    this.getAllProductFields();

    this.checkFields();

    this.excelDataForm.valueChanges.subscribe(() => {
      console.log("hello");
      console.log(this.isAnyFieldFilled);

      this.checkFields();
    });
  }

  checkFields() {
    this.isAnyFieldFilled = Object.values(this.excelDataForm.controls).some(
      (control) => control.value !== null && control.value !== ""
    );
  }

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

  onDownloadExcel(data: any) {
    const formData = this.excelDataForm.value;
    
    const params = {
        department:formData?.department,
        country:formData?.country?.name
    };

    console.log(params);
    

    this.employeeService.getEmployeeReports(params).subscribe(
      (res: any) => {
        this._ticktingService.downloadExcelFile(res, `EmployeeData.xlsx`);
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

    this.departmentService.getDepartments({ status: true }).subscribe((res) => {
      this.department = res.body;
      this.department = this.dropdownService.extractNames(this.department);
    });

    this.countryService.getAllCountries({status:true}).subscribe((res:any)=>{
        this.countries = res;
        console.log(res);
        
    })
  }

  confirmDeleteSelected() {
    this.employeeService.removeEmployee(this.deleteId).subscribe((res) => {
      this.alert();
      this.getEmployees();
      this.deleteProductsDialog = false;
    });
  }



  onCancel() {
    this.visible = false;
    this.excelDataForm.reset();
    this.isAnyFieldFilled = false
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
