import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { AccountService } from "../service/account.service";

@Component({
  selector: "app-account-view",
  templateUrl: "./account-view.component.html",
  styleUrls: ["./account-view.component.scss"],
})
export class AccountViewComponent implements OnInit {
  singleAccount?;
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      this.getSingleAccount(a);
    });
  }

  getSingleAccount(id) {
    this.accountService.getSingleAccount(id).subscribe((res) => {
      this.singleAccount = res;
    });
  }
}
