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
          {
            label: "Dashboard",
            icon: "pi pi-fw pi-home",
            routerLink: ["/"],
            permission: "DASHBOARD",
          },
          {
            label: "User",
            icon: "pi pi-user",
            routerLink: "user/list",
            permission: "CREATE_USER",
          },
          {
            label: "Account",
            icon: "pi pi-users",
            routerLink: "account/list",
            permission: "READ_ACCOUNT",
          },
          {
            label: "Ticket",
            icon: "pi pi-ticket",
            routerLink: "ticket/list",
            permission: "READ_TICKET",
          },
          {
            label: "Awb",
            icon: "pi pi-money-bill",
            routerLink: "awb/list",
            permission: "READ_AWB",
          },
          {
            label: "Department",
            icon: "pi pi-database",
            routerLink: "department/list",
            permission: "READ_DEPARTMENT",
          },
          {
            label: "Department Category",
            icon: "pi pi-user",
            routerLink: "department-category/list",
            permission: "READ_DEPARTMENT_CATEGORY",
          },
          {
            label: "Country",
            icon: "pi pi-flag-fill",
            routerLink: "country/list",
            permission: "READ_COUNTRY",
          },
          {
            label: "City",
            icon: "pi pi-building",
            routerLink: "city/list",
            permission: "READ_CITY",
          },
          {
            label: "Product Type",
            icon: "pi pi-box",
            routerLink: "product-type/list",
            permission: "READ_PRODUCT_TYPE",
          },
          {
            label: "Service Type",
            icon: "pi pi-cog",
            routerLink: "service-type/list",
            permission: "READ_SERVICE_TYPE",
          },
          {
            label: "Permission",
            icon: "pi pi-shield",
            routerLink: "permissions",
            permission: "PERMISSION",
          },
        ],
      },
    ];
  }
}
