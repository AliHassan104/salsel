import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { MenuItem } from "primeng/api";
import { LayoutService } from "./service/app.layout.service";
import { SessionStorageService } from "../components/auth/service/session-storage.service";
import { LoginService } from "../components/auth/service/login.service";
import { UserService } from "../components/auth/usermanagement/service/user.service";

@Component({
  selector: "app-topbar",
  templateUrl: "./app.topbar.component.html",
})
export class AppTopBarComponent implements OnInit {
  items!: MenuItem[];

  @ViewChild("menubutton") menuButton!: ElementRef;

  @ViewChild("topbarmenubutton") topbarMenuButton!: ElementRef;

  @ViewChild("topbarmenu") menu!: ElementRef;

  userDetails;
  loginUserName?;
  loginUser;
  loginUserEmail?;
  role;

  constructor(
    public layoutService: LayoutService,
    private sessionService: SessionStorageService,
    private userSevice: UserService
  ) {}

  ngOnInit(): void {
    this.role = this.sessionService.getRoleName();

    this.userSevice
      .getUserByEmail(localStorage.getItem("loginUserEmail"))
      .subscribe((res: any) => {
        this.userDetails = res;
        this.loginUserName = this.userDetails.name;
        this.loginUser = this.loginUserName.charAt(0).toUpperCase();
        this.loginUserEmail = this.userDetails.email;
        localStorage.setItem("loginUserName", this.userDetails.name);
      });
  }
}
