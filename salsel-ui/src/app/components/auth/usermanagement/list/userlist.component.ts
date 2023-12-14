import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { UserService } from "../service/user.service";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/service/dropdown.service";
import { IUser } from "../model/userDto";
import { Table } from "primeng/table";

@Component({
  selector: "app-userlist",
  templateUrl: "./userlist.component.html",
  styleUrls: ["./userlist.component.scss"],
  providers: [MessageService],
})
export class UserlistComponent implements OnInit {
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;
  deleteId: any;

  constructor(
    private userService: UserService,
    private router: Router,
    private dropdownService: DropdownService,
    private messageService: MessageService
  ) {}

  @ViewChild("filter") filter!: ElementRef;

  users?: IUser;

  loading?;
  deleteDialog?;

  ngOnInit(): void {
    this.getAllUsers();
    this.getAllProductFields();
  }

  getAllUsers() {
    const params = { status: this.activeStatus };
    this.userService.getAllUser(params).subscribe((res: any) => {
      this.users = res;
    });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getAllUsers();
    } else {
      this.activeStatus = false;
      this.getAllUsers();
    }
  }

  //   Get All Product Fields
  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productField = res;
      this.status = this.dropdownService.extractNames(
        this.productField.filter((data) => data.name == "Status")[0]
          .productFieldValuesList
      );
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
    this.userService.deactivateUser(this.deleteId).subscribe((res) => {
      this.alert();
      this.getAllUsers();
      this.deleteDialog = false;
    });
  }

  onDeleteUser(id) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  onEditUser(id) {
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["user"], {
      queryParams: queryParams,
    });
  }

  onActiveUser(id) {
    this.userService.updateUserStatus(id).subscribe((res) => {
      this.success();
      this.selectedStatus = "Active";
      this.onStatusChange(this.selectedStatus);
    });
  }

  success() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Activation Successfull",
    });
  }

  alert() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Deactivation Successfull",
    });
  }
}
