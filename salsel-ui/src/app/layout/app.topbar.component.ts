import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { MenuItem } from "primeng/api";
import { LayoutService } from "./service/app.layout.service";

@Component({
  selector: "app-topbar",
  templateUrl: "./app.topbar.component.html",
})
export class AppTopBarComponent implements OnInit {
  items!: MenuItem[];

  @ViewChild("menubutton") menuButton!: ElementRef;

  @ViewChild("topbarmenubutton") topbarMenuButton!: ElementRef;

  @ViewChild("topbarmenu") menu!: ElementRef;

  loginUserName?;

  constructor(public layoutService: LayoutService) {}

  ngOnInit(): void {
    this.loginUserName = localStorage.getItem("loginUserName");
    this.loginUserName = this.loginUserName.charAt(0).toUpperCase();
  }
}
