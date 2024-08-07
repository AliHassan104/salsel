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
            label: "Employee",
            icon: "pi pi-users",
            routerLink: "employee/list",
            permission: "READ_EMPLOYEE",
          },
          {
            label: "Billing",
            icon: "pi pi-credit-card",
            routerLink: "billing/list",
            permission: "READ_BILLING",
          },
          {
            label: "Account",
            icon: "pi pi-wallet",
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
            label: "Address Book",
            icon: "pi pi-book",
            routerLink: "address-book/list",
            permission: "READ_ADDRESS_BOOK",
          },
          {
            label: "Scan And Tracking",
            icon: "pi pi-map",
            routerLink: "tracking-and-scan",
            permission: "TRACKING_AND_SCAN",
          },
          {
            label: "Department",
            icon: "pi pi-database",
            routerLink: "department/list",
            permission: "READ_DEPARTMENT",
          },
          {
            label: "Department Category",
            icon: "pi pi-server",
            routerLink: "department-category/list",
            permission: "READ_DEPARTMENT_CATEGORY",
          },
          {
            label: "Ticket Category",
            icon: "pi pi-map",
            routerLink: "ticket-category/list",
            permission: "READ_TICKET_CATEGORY",
          },
          {
            label: "Ticket Sub Category",
            icon: "pi pi-server",
            routerLink: "ticket-sub-category/list",
            permission: "READ_TICKET_SUB_CATEGORY",
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
