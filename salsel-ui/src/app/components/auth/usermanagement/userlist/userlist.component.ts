import { Component, ElementRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";

@Component({
  selector: "app-userlist",
  templateUrl: "./userlist.component.html",
  styleUrls: ["./userlist.component.scss"],
})
export class UserlistComponent {
  deleteusersDialog: any;
  constructor(
    private _ticktingService: TicktingService,
    private router: Router
  ) {}

  user: any = [
    {
      id: 1,
      username: "hamza123",
      name: "Hamza Khan",
      email: "hamza123@gmail.com",
      phoneNumber: "+923321233123",
      role: "ROLE_ADMIN",
    },
  ];

  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  tickets: any = [];
  data: any = {};
  deleteId: any;

  ngOnInit(): void {
    this.getUsers();
  }

  //   Get all User
  getUsers() {
    this._ticktingService.getTickets().subscribe((res) => {
      this.tickets = res;
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
  confirmDeleteSelected() {
    this._ticktingService.deleteTicket(this.deleteId).subscribe((res) => {
      this.getUsers();
      this.deleteusersDialog = false;
    });
  }

  //   Delete User

  onDeleteUser(id) {
    this.deleteId = id;
    this.deleteusersDialog = true;
  }

  //   Edit User
  onEditUser(id) {
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["userslist/adduser"], {
      queryParams: queryParams,
    });
  }
}
