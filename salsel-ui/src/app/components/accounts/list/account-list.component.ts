import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { AccountService } from "../service/account.service";
import { Table } from "primeng/table";
import { Router } from "@angular/router";

@Component({
  selector: "app-account-list",
  templateUrl: "./account-list.component.html",
  styleUrls: ["./account-list.component.scss"],
})
export class AccountListComponent implements OnInit {
  @ViewChild("filter") filter!: ElementRef;
  deleteId: any;

  constructor(private accountService: AccountService, private router: Router) {}

  accounts?;

  deleteAccountDialog;
  loading;

  ngOnInit(): void {
    this.getAllAccount();
  }

  //   GET ALL ACCOUNTS
  getAllAccount() {
    this.accountService.getAllAccounts().subscribe((res) => {
      this.accounts = res;
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
    this.router.navigate(["account/addaccount"], {
      queryParams: queryParams,
    });
  }
}
