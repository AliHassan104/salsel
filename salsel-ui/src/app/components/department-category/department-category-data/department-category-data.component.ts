import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Table } from 'primeng/table';

@Component({
  selector: 'app-department-category-data',
  templateUrl: './department-category-data.component.html',
  styleUrls: ['./department-category-data.component.scss']
})
export class DepartmentCategoryDataComponent {
  deleteProductsDialog: any;
  constructor(
    // private _ticktingService: TicktingService,
    private router: Router
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  // tickets: TicketData[];
  tickets: []
  data: any = {};
  deleteId: any;

  ngOnInit(): void {
    this.getTickets();
  }

  //   Get all tickets
  getTickets() {
    // this._ticktingService.getTickets().subscribe((res) => {
    //   this.data = res;
    //   this.tickets = this.data.notes;
    // });
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
    // this._ticktingService.deleteTicket(this.deleteId).subscribe((res) => {
    //   this.getTickets();
      // this.deleteProductsDialog = false;
    // });
  }

  //   Delete Ticket

  onDeleteTicket(id) {
    this.deleteId = id;
    this.deleteProductsDialog = true;
  }

  //   Edit Ticket
  onEditTicket(id) {
  //   console.log(id);
  //   this._ticktingService.editId.next(id);
  //   this.router.navigate(["addticket"]);
  //   this._ticktingService.editTicketMode.next(true);
  }
}
