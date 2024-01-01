import { Component, ElementRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { ServiceTypeService } from "../service/service-type.service";
import { IServiceType } from "src/app/components/service-type/model/serviceType.model";
import { MessageService } from "primeng/api";
import { DropdownService } from "../../../layout/service/dropdown.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";

@Component({
  selector: "app-service-type-list",
  templateUrl: "./service-type-list.component.html",
  styleUrls: ["./service-type-list.component.scss"],
  providers: [MessageService],
})
export class ServiceTypeListComponent {
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  deleteDialog: any;
  serviceType?: IServiceType[];
  deleteId: any;

  constructor(
    private serviceTypeService: ServiceTypeService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getServiceTypes();
    this.getAllProductFields();
  }

  getServiceTypes() {
    const params = { status: this.activeStatus };
    this.serviceTypeService.getServiceTypes(params).subscribe((res) => {
      if (res && res.body) {
        this.serviceType = res.body;
      }
    });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getServiceTypes();
    } else {
      this.activeStatus = false;
      this.getServiceTypes();
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
    this.serviceTypeService
      .removeServiceType(this.deleteId)
      .subscribe((res) => {
        if (res.status == 200) {
          this.alert();
          this.getServiceTypes();
          this.deleteDialog = false;
        }
      });
  }

  deleteServiceType(id?: any) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  editServiceType(id?: any) {
    const queryParams = { id: id };
    this.router.navigate(["create-service-type"], { queryParams: queryParams });
  }

  onActiveServiceType(id) {
    this.serviceTypeService.updateServiceTypeStatus(id).subscribe((res) => {
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
