import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { TicktingService } from "src/app/components/Tickets/tickting.service";
import { Table } from "primeng/table";
import { Router } from "@angular/router";
import { Ticket } from "src/app/api/ticket";

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
  tickets: any = [];
  data: any = {};
  deleteId: any;

  ngOnInit(): void {
    this.getTickets();
  }

  //   Get all tickets
  getTickets() {
    this._ticktingService.getTickets().subscribe((res) => {
      this.tickets = res;
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
    this._ticktingService.editId.next(id);
    this.router.navigate(["tickets/addticket"]);
    this._ticktingService.editTicketMode.next(true);
  }
}
