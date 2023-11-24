import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { TicketData } from "src/app/demo/api/ticketdata";
import { TicktingService } from "src/app/demo/service/tickting.service";
import { Table } from "primeng/table";
import { ShipperDetails } from "../../demo/api/ticketdata";
import { Router } from "@angular/router";

@Component({
  selector: "app-ticketsdata",
  templateUrl: "./ticketsdata.component.html",
  styleUrls: ["./ticketsdata.component.scss"],
})
export class TicketsdataComponent implements OnInit {
  deleteProductsDialog: any;
  constructor(
    private _ticktingService: TicktingService,
    private router: Router
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  tickets: TicketData[];
  data: any = {};
  deleteId: any;

  ngOnInit(): void {
    this.getTickets();
  }

  //   Get all tickets
  getTickets() {
    this._ticktingService.getTickets().subscribe((res) => {
      this.data = res;
      this.tickets = this.data.notes;
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
    this._ticktingService.deleteTicket(this.deleteId).subscribe((res) => {
      this.getTickets();
      this.deleteProductsDialog = false;
    });
  }

  //   Delete Ticket

  onDeleteTicket(id) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  //   Edit Ticket
  onEditTicket(id) {
    console.log(id);
    this._ticktingService.editId.next(id);
    this.router.navigate(["addticket"]);
    this._ticktingService.editTicketMode.next(true);
  }
}
