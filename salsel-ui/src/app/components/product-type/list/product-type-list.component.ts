import { Component, ElementRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { IProductType } from "src/app/components/product-type/model/productType.model";
import { ProductTypeService } from "../service/product-type.service";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/service/dropdown.service";
@Component({
  selector: "app-product-type-list",
  templateUrl: "./product-type-list.component.html",
  styleUrls: ["./product-type-list.component.scss"],
  providers: [MessageService],
})
export class ProductTypeListComponent {
  productField?;
  status?;
  selectedStatus: string = "Active";
  activeStatus: boolean = true;

  deleteDialog: any;
  productTypes?: IProductType[];
  deleteId: any;

  constructor(
    private productTypeService: ProductTypeService,
    private router: Router,
    private MessageService: MessageService,
    private dropdownService: DropdownService
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getProductTypes();
    this.getAllProductFields();
  }

  getProductTypes() {
    const params = { status: this.activeStatus };
    this.productTypeService.getProductTypes(params).subscribe((res) => {
      if (res && res.body) {
        this.productTypes = res.body;
        console.log(this.productTypes);
      }
    });
  }

  onStatusChange(data) {
    if (data == "Active") {
      this.activeStatus = true;
      this.getProductTypes();
    } else {
      this.activeStatus = false;
      this.getProductTypes();
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
    this.productTypeService
      .removeProductType(this.deleteId)
      .subscribe((res) => {
        if (res.status == 200) {
          this.alert();
          this.getProductTypes();
          this.deleteDialog = false;
        }
      });
  }

  deleteProductType(id?: any) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  editProductType(id?: any) {
    const queryParams = { id: id };
    this.router.navigate(["product-type"], { queryParams: queryParams });
  }

  onActiveproductType(id) {
    this.productTypeService.updateProductTypeStatus(id).subscribe((res) => {
      this.success();
      this.selectedStatus = "Active";
      this.onStatusChange(this.selectedStatus);
    });
  }

  success() {
    this.MessageService.add({
      severity: "success",
      summary: "Success",
      detail: "Activation Successfull",
    });
  }

  alert() {
    this.MessageService.add({
      severity: "success",
      summary: "Success",
      detail: "Deactivation Successfull",
    });
  }
}
