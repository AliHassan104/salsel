import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { AccountService } from "../service/account.service";
import { MessageService } from "primeng/api";

@Component({
  selector: "app-account-view",
  templateUrl: "./account-view.component.html",
  styleUrls: ["./account-view.component.scss"],
  providers: [MessageService],
})
export class AccountViewComponent implements OnInit {
  singleAccount?;
  id: any;
  agreementUrl;
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      this.id = a;
      this.getSingleAccount(a);
    });
  }

  downloadAgreement() {
    this.accountService.downloadAgreement(this.agreementUrl).subscribe(
      (res: any) => {
        this.downloadSuccess();
        this.accountService.downloadFile(
          res,
          `Agreement_${this.singleAccount?.accountNumber}`
        );
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

  getSingleAccount(id) {
    this.accountService.getSingleAccount(id).subscribe((res: any) => {
      this.singleAccount = res;
      this.agreementUrl = this.singleAccount.accountUrl;
    });
  }
}
