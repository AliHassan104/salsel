import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";
import { Table } from "primeng/table";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/service/dropdown.service";
import { Ticket } from "src/app/components/Tickets/model/ticketValuesDto";

@Component({
  selector: "app-ticketsdata",
  templateUrl: "./ticketsdata.component.html",
  styleUrls: ["./ticketsdata.component.scss"],
  providers: [MessageService],
})
export class TicketsdataComponent implements OnInit {
  //   Activity Work
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  deleteProductsDialog: any;
  serachText?: string;
  constructor(
    private _ticktingService: TicktingService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService
  ) {}

  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  tickets?: Ticket;
  deleteId: any;
  page?: any = 0;
  size?: number = 1;
  totalRecords?: number;

  ngOnInit(): void {
    this.getTickets();
    this.getAllProductFields();
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
      status: this.activeStatus,
    };

    this._ticktingService.getTickets(queryParams).subscribe((res: any) => {
      if (res && res.body) {
        this.tickets = res.body.content;
        this.totalRecords = res.body.totalElements;
        console.log(res.body.content);
      }
    });
  }

  onPageChange(event?: any) {
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

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getTickets();
    } else {
      this.activeStatus = false;
      this.getTickets();
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

  confirmDeleteSelected() {
    this._ticktingService.deleteTicket(this.deleteId).subscribe((res) => {
      this.alert();
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
    this.router.navigate(["ticket"], {
      queryParams: queryParams,
    });
  }

  onActiveTicket(id) {
    this._ticktingService.updateTicketStatus(id).subscribe((res) => {
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
