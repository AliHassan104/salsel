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
            label: "Profile",
            icon: "pi pi-user",
            routerLink: "profile",
            permission: "FREE_ACCESS",
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
            label: "Booking",
            icon: "pi pi-calendar",
            routerLink: "booking/list",
            permission: "READ_TICKET",
          },
          {
            label: "Address Book",
            icon: "pi pi-book",
            routerLink: "address-book/list",
            permission: "READ_ADDRESS_BOOK",
          },
        ],
      },
    ];
  }
}
