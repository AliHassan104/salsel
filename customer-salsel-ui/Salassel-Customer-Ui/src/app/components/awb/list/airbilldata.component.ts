import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { AirbillService } from "../service/airbill.service";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { IAwbDto } from "src/app/components/awb/model/awbValuesDto";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { finalize } from "rxjs";

@Component({
  selector: "app-airbilldata",
  templateUrl: "./airbilldata.component.html",
  styleUrls: ["./airbilldata.component.scss"],
  providers: [MessageService],
})
export class AirbilldataComponent implements OnInit {
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;
  refresh: boolean = true;

  deleteProductsDialog: any;
  constructor(
    private _airbillService: AirbillService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  bills: IAwbDto;
  deleteId: any;

  ngOnInit(): void {
    this.getAirbills();
    this.getAllProductFields();
  }

  getAirbills() {
    const params = { status: this.activeStatus };

    if (
      this.sessionStorageService.getRoleName() == "ADMIN" ||
      this.sessionStorageService.getRoleName() == "CUSTOMER_SERVICE_AGENT"
    ) {
      this._airbillService
        .getBills(params)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          this.bills = res;
        });
    } else {
      this._airbillService
        .getBillsByUserEmailAndRole(params)
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          this.bills = res;
        });
    }
  }

  onRefresh() {
    this.refresh = true;
    this.getAirbills();
  }

  onStatusChange(data?: any) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getAirbills();
    } else {
      this.activeStatus = false;
      this.getAirbills();
    }
  }

  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res?: any) => {
      this.productField = res;
      this.status = this.dropdownService.extractNames(
        this.productField.filter((data) => data?.name == "Status")[0]
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
    this._airbillService.deleteBill(this.deleteId).subscribe((res?: any) => {
      this.alert();
      this.getAirbills();
      this.deleteProductsDialog = false;
    });
  }

  //   Delete Ticket

  onDeleteBill(id?: any) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  //   Edit Bill
  onEditBill(id?: any) {
    this._airbillService.updateAWB.next(true);
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["create-awb"], {
      queryParams: queryParams,
    });
  }

  onDownloadBill(id?: any, num?: any) {
    this._airbillService.downloadBill(id).subscribe(
      (res: any) => {
        this.downloadSuccess();
        this._airbillService.downloadFile(res, `Awb_${num}.pdf`);
      },
      (error) => {
        this.downloadError();
      }
    );
  }

  onActiveBill(id) {
    this._airbillService.updateBillStatus(id).subscribe((res) => {
      this.success();
      this.selectedStatus = "Active";
      this.onStatusChange(this.selectedStatus);
    });
  }

  onViewHistory(id) {}

  success() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Activation Successfull",
    });
  }

  downloadError() {
    this.messageService.add({
      severity: "error",
      summary: "Error",
      detail: "Download Failed",
    });
  }

  downloadSuccess() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "File Successfully Downloaded",
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
