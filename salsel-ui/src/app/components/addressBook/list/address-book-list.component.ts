import { Component, ElementRef, ViewChild } from "@angular/core";
import { IAddressBook } from "../model/addressBookDto";
import { AddressBookService } from "../service/address-book.service";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { Table } from "primeng/table";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { CountryService } from "../../country/service/country.service";
import { finalize } from "rxjs";

@Component({
  selector: "app-address-book-list",
  templateUrl: "./address-book-list.component.html",
  styleUrls: ["./address-book-list.component.scss"],
  providers: [MessageService],
})
export class AddressBookListComponent {
  productField?;
  status?;
  userType;
  selectedStatus: string = "Active";
  selectedUserType: string = "Both";
  activeStatus: boolean = true;
  refresh: boolean = false;

  deleteDialog: any;
  addressBooks?: IAddressBook[];
  deleteId: any;

  constructor(
    private countryService: CountryService,
    private adddressBookService: AddressBookService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getAddressBooks();
    this.getAllProductFields();
  }

  getAddressBooks() {
    const params = { status: this.activeStatus };
    this.adddressBookService
      .getAddressBooks(params)
      .pipe(
        finalize(() => {
          this.refresh = false;
        })
      )
      .subscribe((res) => {
        if (res && res.body) {
          this.addressBooks = res.body;
        }
      });
  }

  getAddressBookByUserType() {
    const params = {
      userType: this.selectedUserType,
      status: this.activeStatus,
    };
    this.adddressBookService
      .getAddressBooksByUserType(params)
      .pipe(
        finalize(() => {
          this.refresh = false;
        })
      )
      .subscribe((res) => {
        if (res && res.body) {
          this.addressBooks = res.body;
        }
      });
  }

  onRefresh() {
    if (this.selectedUserType == "Both") {
      this.refresh = true;
      this.getAddressBooks();
    } else {
      this.refresh = true;
      this.getAddressBookByUserType();
    }
  }

  onUserTypeChange(data) {
    if (data == "Both") {
      this.activeStatus = true;
      this.getAddressBooks();
    } else if (data == "Shipper") {
      this.activeStatus = true;
      this.getAddressBookByUserType();
    } else {
      this.activeStatus = true;
      this.getAddressBookByUserType();
    }
  }

  onStatusChange(data) {
    if (data == "Active" && this.selectedUserType == "Both") {
      this.activeStatus = true;
      this.getAddressBooks();
    } else if (data == "In Active" && this.selectedUserType == "Both") {
      this.activeStatus = false;
      this.getAddressBooks();
    } else if (data == "Active" && this.selectedUserType == "Shipper") {
      this.activeStatus = true;
      this.getAddressBookByUserType();
    } else if (data == "In Active" && this.selectedUserType == "Shipper") {
      this.activeStatus = false;
      this.getAddressBookByUserType();
    } else if (data == "Active" && this.selectedUserType == "Recipient") {
      this.activeStatus = true;
      this.getAddressBookByUserType();
    } else if (data == "In Active" && this.selectedUserType == "Recipient") {
      this.activeStatus = false;
      this.getAddressBookByUserType();
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

      this.userType = this.dropdownService.extractNames(
        this.productField.filter((data) => data.name == "User Type List")[0]
          .productFieldValuesList
      );
    });
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = "";
  }
  confirmDeleteSelected() {
    this.adddressBookService
      .removeAddressBook(this.deleteId)
      .subscribe((res) => {
        if (res.status == 200) {
          if (this.selectedUserType == "Both") {
            this.getAddressBooks();
            this.deleteDialog = false;
          } else {
            this.getAddressBookByUserType();
            this.deleteDialog = false;
          }
        }
      });
  }

  deleteAddressBook(id?: any) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  editAddressBook(id?: any) {
    const queryParams = { id: id };
    this.router.navigate(["create-address-book"], {
      queryParams: queryParams,
    });
  }

  onActiveAddressBook(id) {
    this.adddressBookService.updateAddressBookStatus(id).subscribe((res) => {
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
