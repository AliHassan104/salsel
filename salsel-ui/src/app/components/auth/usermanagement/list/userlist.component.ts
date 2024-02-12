import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { UserService } from "../service/user.service";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { IUser } from "../model/userDto";
import { Table } from "primeng/table";
import { SessionStorageService } from "../../service/session-storage.service";
import { filter, finalize } from "rxjs";
import { DatePipe } from "@angular/common";
import { FormvalidationService } from "src/app/components/Tickets/service/formvalidation.service";
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: "app-userlist",
  templateUrl: "./userlist.component.html",
  styleUrls: ["./userlist.component.scss"],
  providers: [MessageService, DatePipe],
})
export class UserlistComponent implements OnInit {
  excelDataForm!: FormGroup;
  minDate;
  maxDate;
  visible;
  productField?;
  status?;
  userFilter?;
  selectedStatus: string = "Active";
  selectedUsers: string = "Employee";
  activeStatus: boolean = true;
  deleteId: any;
  generateId: any;
  getPass: string = "Copy";

  refresh: boolean = true;

  constructor(
    private userService: UserService,
    private router: Router,
    private dropdownService: DropdownService,
    private messageService: MessageService,
    public sessionStorageService: SessionStorageService,
    private formService: FormvalidationService,
    private datePipe: DatePipe
  ) {}

  @ViewChild("filter") filter!: ElementRef;

  users?: IUser;

  loading?;
  deleteDialog?;
  generateDialog;
  regeneratedDialog?;
  generatedPassword?;

  ngOnInit(): void {
    this.excelDataForm = new FormGroup({
      toDate: new FormControl(null, Validators.required),
      fromDate: new FormControl(null, Validators.required),
    });
    this.getAllUsers();
    this.getAllProductFields();
    this.getMinMax();
  }

  getMinMax() {
    this.userService.getMinMax().subscribe((res: any) => {
      this.minDate = new Date(res.minDate);
      this.maxDate = new Date(res.maxDate);
    });
  }

  getAllUsers() {
    const params = { status: this.activeStatus };
    const employeeRoles = [
      "ROLE_ADMIN",
      "ROLE_OPERATION_AGENT",
      "ROLE_CUSTOMER_SERVICE_AGENT",
      "ROLE_SALES_AGENT",
      "ROLE_CUSTOMER_CARE_AGENT",
      "ROLE_MANAGEMENT_USER",
      "ROLE_OPERATION_USER",
      "ROLE_COURIER",
    ];
    const customerRoles = ["ROLE_ACCOUNT_HOLDER", "ROLE_CUSTOMER_USER"];
    this.userService
      .getAllUser(params)
      .pipe(
        finalize(() => {
          this.refresh = false;
        })
      )
      .subscribe((res: any) => {
        if (this.selectedUsers == "Employee") {
          this.users = res.filter((user) =>
            user?.roles?.some((role) => employeeRoles.includes(role?.name))
          );
        } else if (this.selectedUsers == "Customer") {
          this.users = res.filter((user) =>
            user?.roles?.some((role) => customerRoles.includes(role?.name))
          );
        } else {
          this.users = res.filter(
            (user) => !user?.roles || user.roles.length === 0
          );
        }
      });
  }

  onRefresh() {
    this.refresh = true;
    this.getAllUsers();
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getAllUsers();
    } else {
      this.activeStatus = false;
      this.getAllUsers();
    }
  }

  onFilterUser(data) {
    this.getAllUsers();
  }

  onDownloadExcel(data: any) {
    if (this.excelDataForm.valid) {
      const formattedDates = {
        startDate: this.datePipe.transform(data.fromDate, "yyyy-MM-dd"),
        endDate: this.datePipe.transform(data.toDate, "yyyy-MM-dd"),
      };
      console.log(formattedDates);

      this.userService
        .downloadUserDataInExcel(formattedDates)
        .pipe(
          finalize(() => {
            this.messageService.add({
              severity: "success",
              summary: "Success",
              detail: "Download Successfull",
            }),
              this.excelDataForm.reset();
            this.visible = false;
          })
        )
        .subscribe((res: any) => {
          this.userService.downloadExcelFile(
            res,
            `Ticket${formattedDates.startDate}_to_${formattedDates.endDate}.xlsx`
          );
          (error) => {
            this.messageService.add({
              severity: "error",
              summary: "Error",
              detail: "Try Again After Few Mins",
            });
          };
        });
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

  copyToClipboard() {
    const textToCopy = this.generatedPassword;
    const dummyElement = document.createElement("textarea");

    dummyElement.value = textToCopy;

    document.body.appendChild(dummyElement);

    dummyElement.select();
    dummyElement.setSelectionRange(0, 99999); // For mobile devices

    document.execCommand("copy");

    document.body.removeChild(dummyElement);

    this.messageService.add({
      severity: "success",
      summary: "Copied",
    });

    // Optionally, you can display a message or perform other actions after copying
  }

  //   Get All Product Fields
  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res: any) => {
      this.productField = res;
      this.status = this.dropdownService.extractNames(
        this.productField.filter((data) => data?.name == "Status")[0]
          .productFieldValuesList
      );
      this.userFilter = this.dropdownService.extractNames(
        this.productField.filter((data) => data?.name == "User Filter")[0]
          .productFieldValuesList
      );
    });
  }

  //   For table filtering purpose
  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = "";
  }

  confirmDeleteSelected() {
    this.userService.deactivateUser(this.deleteId).subscribe((res) => {
      this.alert();
      this.getAllUsers();
      this.deleteDialog = false;
    });
  }

  onDeleteUser(id) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  confirmGeneratePassword() {
    this.generateDialog = false;
    this.userService.regeneratePassword(this.generateId).subscribe(
      (res: any) => {
        this.generatedPassword = res;
        this.regeneratedDialog = true;
      },
      (error: any) => {
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: "An error occurred. Please try again in a few minutes.",
        });
      }
    );
  }

  onGeneratePassword(id: any) {
    this.generateId = id;
    this.generateDialog = true;
  }

  onEditUser(id) {
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["create-user"], {
      queryParams: queryParams,
    });
  }

  onActiveUser(id) {
    this.userService.updateUserStatus(id).subscribe((res: any) => {
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
