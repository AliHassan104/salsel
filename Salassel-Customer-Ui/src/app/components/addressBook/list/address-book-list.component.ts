import { Component, ElementRef, ViewChild } from '@angular/core';
import { IAddressBook } from '../model/addressBookDto';
import { CountryService } from 'src/app/service/country.service';
import { AddressBookService } from '../service/address-book.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { DropdownService } from 'src/app/layout/service/dropdown.service';
import { SessionStorageService } from '../../auth/service/session-storage.service';

@Component({
  selector: "app-address-book-list",
  templateUrl: "./address-book-list.component.html",
  styleUrls: ["./address-book-list.component.scss"],
  providers: [MessageService],
})
export class AddressBookListComponent {
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

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
    this.adddressBookService.getAddressBooks(params).subscribe((res) => {
      if (res && res.body) {
        this.addressBooks = res.body;
      }
    });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getAddressBooks();
    } else {
      this.activeStatus = false;
      this.getAddressBooks();
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
          this.getAddressBooks();
          this.deleteDialog = false;
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

  //   onActiveAddressBook(id) {
  //     this.adddressBookService
  //       .updateAddressBookStatus(id)
  //       .subscribe((res) => {
  //         this.success();
  //         this.selectedStatus = "Active";
  //         this.onStatusChange(this.selectedStatus);
  //       });
  //   }

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
