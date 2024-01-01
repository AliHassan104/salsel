import { Component, ElementRef, OnInit, ViewChild, Input } from "@angular/core";
import { AccountService } from "../service/account.service";
import { Table } from "primeng/table";
import { Router } from "@angular/router";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { MessageService } from "primeng/api";
import { IAccountData } from "src/app/components/accounts/model/accountValuesDto";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { UploadEvent } from "primeng/fileupload";

@Component({
  selector: "app-account-list",
  templateUrl: "./account-list.component.html",
  styleUrls: ["./account-list.component.scss"],
  providers: [MessageService],
})
export class AccountListComponent implements OnInit {
  @ViewChild("filter") filter!: ElementRef;
  //   Activity Work
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  file;

  deleteId: any;

  @ViewChild("fileUpload", { static: false }) fileUpload: any;

  constructor(
    private accountService: AccountService,
    private router: Router,
    private dropdownService: DropdownService,
    private messageService: MessageService,
    public sessionStorageService: SessionStorageService
  ) {}

  accounts?: IAccountData[];
  deleteAccountDialog;
  loading;

  ngOnInit(): void {
    this.getAllAccount();
    this.getAllProductFields();
  }

  //   GET ALL ACCOUNTS
  getAllAccount() {
    const params = { status: this.activeStatus };
    this.accountService.getAllAccounts(params).subscribe((res) => {
      if (res && res.body) {
        this.accounts = res.body;
      }
    });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getAllAccount();
    } else {
      this.activeStatus = false;
      this.getAllAccount();
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

  //   For table filtering purpose
  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = "";
  }

  //   Confirmation message for delete country

  confirmDeleteSelected() {
    this.accountService.deleteAccount(this.deleteId).subscribe((res) => {
      this.alert();
      this.getAllAccount();
      this.deleteAccountDialog = false;
    });
  }

  //   Delete Account

  onDeleteAccount(id) {
    this.deleteId = id;
    this.deleteAccountDialog = true;
  }

  //   Edit Account
  onEditAccount(id) {
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["create-account"], {
      queryParams: queryParams,
    });
  }

  onActiveAccount(id) {
    this.accountService.updateAccountStatus(id).subscribe((res) => {
      this.success();
      this.selectedStatus = "Active";
      this.onStatusChange(this.selectedStatus);
    });
  }

  onDownloadAgreement(url, id) {
    this.accountService.downloadAgreement(url).subscribe(
      (res: any) => {
        this.downloadSuccess();
        this.accountService.downloadFile(res, `Agreement_${id}`);
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
