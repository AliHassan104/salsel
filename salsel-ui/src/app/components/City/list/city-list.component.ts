import { MessageService } from "primeng/api";
import { Component, ElementRef, ViewChild } from "@angular/core";
import { CityService } from "../service/city.service";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";

@Component({
  selector: "app-city-list",
  templateUrl: "./city-list.component.html",
  styleUrls: ["./city-list.component.scss"],
  providers: [MessageService],
})
export class CityListComponent {
  constructor(
    private _cityService: CityService,
    private router: Router,
    private dropdownService: DropdownService,
    private messageService: MessageService,

    public sessionStorageService: SessionStorageService
  ) {}

  @ViewChild("filter") filter!: ElementRef;

  productField?;
  cities?;
  deleteId?;
  status?;
  selectedStatus: any = "Active";
  loading: any;
  deleteCityDialog: any;
  activeStatus: boolean = true;

  ngOnInit(): void {
    this.getAllCities();
    this.getAllProductFields();
  }

  //   GET ALL COUNTIRES DATA

  getAllCities() {
    const params = { status: this.activeStatus };

    this._cityService.getAllCities(params).subscribe((res: any) => {
      this.cities = res;
    });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getAllCities();
    } else {
      this.activeStatus = false;
      this.getAllCities();
    }
  }

  //   Get All Product Fields
  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res: any) => {
      this.productField = res;
      this.status = this.dropdownService.extractNames(
        this.productField.filter((data) => data?.name == "Status")[0]
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
    this._cityService.deleteCity(this.deleteId).subscribe((res: any) => {
      this.alert();
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
    this.router.navigate(["create-city"], {
      queryParams: queryParams,
    });
  }

  onActiveCity(id) {
    this._cityService.updateCityStatus(id).subscribe((res: any) => {
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
