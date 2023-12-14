import { OnInit } from "@angular/core";
import { Component } from "@angular/core";
import { LayoutService } from "./service/app.layout.service";
import { SessionStorageService } from "../components/auth/service/session-storage.service";

@Component({
  selector: "app-menu",
  templateUrl: "./app.menu.component.html",
})
export class AppMenuComponent implements OnInit {
  model: any[] = [];

  constructor(
    public layoutService: LayoutService,
    public sessionStorageService: SessionStorageService
  ) {}

  ngOnInit() {
    this.sessionStorageService.updatePermission();
    this.model = [
      {
        label: "Home",
        items: [
          { label: "Dashboard", icon: "pi pi-fw pi-home", routerLink: ["/"] },
          {
            label: "User",
            icon: "pi pi-user",
            routerLink: "user/list",
          },
          {
            label: "Account",
            icon: "pi pi-users",
            routerLink: "account/list",
          },
          {
            label: "Ticket",
            icon: "pi pi-ticket",
            routerLink: "ticket/list",
          },
          {
            label: "Awb",
            icon: "pi pi-money-bill",
            routerLink: "awb/list",
          },
          {
            label: "Department",
            icon: "pi pi-database",
            routerLink: "department/list",
          },
          {
            label: "Department Category",
            icon: "pi pi-user",
            routerLink: "department-category/list",
          },
          {
            label: "Country",
            icon: "pi pi-flag-fill",
            routerLink: "country/list",
          },
          {
            label: "City",
            icon: "pi pi-building",
            routerLink: "city/list",
          },
          {
            label: "Product Type",
            icon: "pi pi-box",
            routerLink: "product-type/list",
          },
          {
            label: "Service Type",
            icon: "pi pi-cog",
            routerLink: "service-type/list",
          },
          {
            label: "Permission",
            icon: "pi pi-shield",
            routerLink: "permissions",
          },
        ],
      },
    ];
  }
}
