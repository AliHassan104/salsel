import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";
import { Table } from "primeng/table";
import { Router } from "@angular/router";

@Component({
  selector: "app-ticketsdata",
  templateUrl: "./ticketsdata.component.html",
  styleUrls: ["./ticketsdata.component.scss"],
})
export class TicketsdataComponent implements OnInit {
  deleteProductsDialog: any;
  serachText?: string;
  constructor(
    private _ticktingService: TicktingService,
    private router: Router
  ) {}

  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  tickets: any = [];
  deleteId: any;
  page?: any = 0;
  size?: number = 1;
  totalRecords?: number;

  ngOnInit(): void {
    this.getTickets();
  }

  //   Get all tickets
  getTickets() {
    let search = {
      searchText: this.serachText,
    };

    const queryParams = {
      page: this.page,
      size: this.size,
      sort: "id",
      search: JSON.stringify(search),
    };

    this._ticktingService.getTickets(queryParams).subscribe((res: any) => {
      if (res && res.body) {
        this.tickets = res.body.content;
        this.totalRecords = res.body.totalElements;
      }
    });
  }

  onPageChange(event?: any) {
    debugger;
    this.page = event.page;
    this.getTickets();
  }

  //   For table filtering purpose
  onGlobalFilter(table: Table, event: any) {
    // Update the searchText property in the search object
    this.serachText = event.target.value;
    this.getTickets();
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
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["tickets/createticket"], {
      queryParams: queryParams,
    });
  }
}
