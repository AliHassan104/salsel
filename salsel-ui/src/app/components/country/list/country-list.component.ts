import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { CountryService } from "../service/country.service";
import { Table } from "primeng/table";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { MessageService } from "primeng/api";
import { SessionStorageService } from "../../auth/service/session-storage.service";

@Component({
  selector: "app-country-list",
  templateUrl: "./country-list.component.html",
  styleUrls: ["./country-list.component.scss"],
  providers: [MessageService],
})
export class CountryListComponent implements OnInit {
  //   Activity Work
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  constructor(
    private _countryService: CountryService,
    private router: Router,
    private dropdownService: DropdownService,
    private messageService: MessageService,

    public sessionStorageService: SessionStorageService
  ) {}

  @ViewChild("filter") filter!: ElementRef;

  countries?;
  deleteId?;
  loading: any;
  deleteCountryDialog: any;

  ngOnInit(): void {
    this.getAllCountries();
    this.getAllProductFields();
  }

  //   GET ALL COUNTIRES DATA

  getAllCountries() {
    const params = { status: this.activeStatus };

    this._countryService.getAllCountries(params).subscribe((res) => {
      this.countries = res;
    });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getAllCountries();
    } else {
      this.activeStatus = false;
      this.getAllCountries();
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

  //   Confirmation message for delete country

  confirmDeleteSelected() {
    this._countryService.deleteCountry(this.deleteId).subscribe((res) => {
      this.alert();
      this.getAllCountries();
      this.deleteCountryDialog = false;
    });
  }

  //   Delete Country

  onDeleteCountry(id) {
    this.deleteId = id;
    this.deleteCountryDialog = true;
  }

  //   Edit Ticket
  onEditCountry(id) {
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["create-country"], {
      queryParams: queryParams,
    });
  }

  onActiveCountry(id) {
    this._countryService.updateCountryStatus(id).subscribe((res) => {
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
