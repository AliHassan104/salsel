import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { AirbillService } from "../service/airbill.service";
import { MessageService } from "primeng/api";

@Component({
  selector: "app-airbilldata",
  templateUrl: "./airbilldata.component.html",
  styleUrls: ["./airbilldata.component.scss"],
  providers: [MessageService],
})
export class AirbilldataComponent implements OnInit {
  deleteProductsDialog: any;
  constructor(
    private _airbillService: AirbillService,
    private router: Router,
    private messageSerice: MessageService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  bills: any = [];
  data: any = {};
  deleteId: any;

  ngOnInit(): void {
    this.getAirbills();
  }

  //   Get all tickets
  getAirbills() {
    this._airbillService.getBills().subscribe((res) => {
      this.bills = res;
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
    this._airbillService.deleteBill(this.deleteId).subscribe((res) => {
      this.getAirbills();
      this.deleteProductsDialog = false;
    });
  }

  //   Delete Ticket

  onDeleteBill(id) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  //   Edit Ticket
  onEditBill(id) {
    this._airbillService.updateAWB.next(true);
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["airwaybills/createairbill"], {
      queryParams: queryParams,
    });
  }
}
