import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { CountryService } from "../service/country.service";
import { Table } from "primeng/table";

@Component({
  selector: "app-country-list",
  templateUrl: "./country-list.component.html",
  styleUrls: ["./country-list.component.scss"],
})
export class CountryListComponent implements OnInit {
  constructor(
    private _countryService: CountryService,
    private router: Router
  ) {}

  @ViewChild("filter") filter!: ElementRef;

  countries?;
  deleteId?;
  loading: any;
  deleteCountryDialog: any;

  ngOnInit(): void {
    this.getAllCountries();
  }

  //   GET ALL COUNTIRES DATA

  getAllCountries() {
    this._countryService.getAllCountries().subscribe((res) => {
      this.countries = res;
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
    this.router.navigate(["country/addcountry"], {
      queryParams: queryParams,
    });
  }
}
