import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { AirbillService } from "../service/airbill.service";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/service/dropdown.service";
import { IAwbDto } from "src/app/components/awb/model/awbValuesDto";

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

  deleteProductsDialog: any;
  constructor(
    private _airbillService: AirbillService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService
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

    this._airbillService.getBills(params).subscribe((res) => {
      this.bills = res;
    });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getAirbills();
    } else {
      this.activeStatus = false;
      this.getAirbills();
    }
  }

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
    this._airbillService.deleteBill(this.deleteId).subscribe((res) => {
      this.alert();
      this.getAirbills();
      this.deleteProductsDialog = false;
    });
  }

  //   Delete Ticket

  onDeleteBill(id) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  //   Edit Bill
  onEditBill(id) {
    this._airbillService.updateAWB.next(true);
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["awb"], {
      queryParams: queryParams,
    });
  }

  onActiveBill(id) {
    this._airbillService.updateBillStatus(id).subscribe((res) => {
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
