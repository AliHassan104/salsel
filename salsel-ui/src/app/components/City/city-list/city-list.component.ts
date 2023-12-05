import { Component, ElementRef, ViewChild } from "@angular/core";
import { CityService } from "../service/city.service";
import { Router } from "@angular/router";
import { Table } from "primeng/table";

@Component({
  selector: "app-city-list",
  templateUrl: "./city-list.component.html",
  styleUrls: ["./city-list.component.scss"],
})
export class CityListComponent {
  constructor(private _cityService: CityService, private router: Router) {}

  @ViewChild("filter") filter!: ElementRef;

  cities?;
  deleteId?;
  loading: any;
  deleteCityDialog: any;

  ngOnInit(): void {
    this.getAllCities();
  }

  //   GET ALL COUNTIRES DATA

  getAllCities() {
    this._cityService.getAllCities().subscribe((res) => {
      this.cities = res;
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
    this._cityService.deleteCity(this.deleteId).subscribe((res) => {
      this.getAllCities();
      this.deleteCityDialog = false;
    });
  }

  //   Delete Country

  onDeleteCity(id) {
    this.deleteId = id;
    this.deleteCityDialog = true;
  }

  //   Edit Ticket
  onEditCity(id) {
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["city/addcity"], {
      queryParams: queryParams,
    });
  }
}
