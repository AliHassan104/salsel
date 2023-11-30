import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { Table } from "primeng/table";
import { Router } from "@angular/router";
import { ProductFieldService } from "../service/product-service.service";
import { IProductFieldDto } from "src/app/api/productFieldDto";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent {
  deleteProductsDialog: any;
  constructor(
    private productFieldService?: ProductFieldService,
    private router?: Router
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;
  productFields?: IProductFieldDto[];
  data: any = {};
  deleteId: any;

  ngOnInit(): void {
    this.getProductFields();
  }

  //   Get all tickets
  getProductFields() {
    this.productFields = [];
    this.productFieldService.getProductFields().subscribe((res) => {
      if(res && res.body){
        this.productFields = res.body;
      }
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
    // this._ticktingService.deleteTicket(this.deleteId).subscribe((res) => {
    //   this.getTickets();
    //   this.deleteProductsDialog = false;
    // });
  }

  //   Delete Ticket

  deleteProductField(id?: any) {
    this.productFieldService.removeProductField(id).subscribe(res =>{
      debugger;
      if(res.status == 200){
        this.getProductFields();
      }
    })
  }

  //   Edit Ticket
  editProductField(id?: any) {
    const queryParams = { id: id };
    this.router.navigate(["product"], {queryParams: queryParams,})
  }
}
