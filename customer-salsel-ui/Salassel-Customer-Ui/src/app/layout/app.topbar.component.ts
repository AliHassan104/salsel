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
  public loginUserName?;
  public loginUser;
  public loginUserEmail?;
  role;

  constructor(
    public layoutService: LayoutService,
    private sessionService: SessionStorageService,
    private userSevice: UserService
  ) {
    this.userSevice.loginUserName.subscribe((res) => {
      this.loginUserName = res;
    });

    this.userSevice.loginUserEmail.subscribe((res) => {
      this.loginUserEmail = res;
    });

    this.userSevice.loginUser.subscribe((res) => {
      this.loginUser = res;
    });
  }

  ngOnInit(): void {
    this.role = this.sessionService.getRoleName();

    this.userSevice
      .getUserByEmail(localStorage.getItem("loginUserEmail"))
      .subscribe((res: any) => {
        this.userDetails = res;
        this.userSevice.loginUserName.next(this.userDetails.name);
        this.userSevice.loginUserEmail.next(this.userDetails.email);
        this.userSevice.loginUser.next(
          this.userDetails.name.charAt(0).toUpperCase()
        );
        localStorage.setItem("loginUserName", this.userDetails.name);
      });
  }
}
