import { Component, ElementRef, ViewChild } from "@angular/core";
import { DepartmentService } from "../../department/service/department.service";
import { IDepartment } from "src/app/api/department.model";
import { Router } from "@angular/router";
import { Table } from "primeng/table";
import { IProductType } from "src/app/api/productType.model";
import { ProductTypeRoutingModule } from "../product-type-routing-module";
import { ProductTypeService } from "../service/product-type.service";
@Component({
  selector: 'app-product-type-list',
  templateUrl: './product-type-list.component.html',
  styleUrls: ['./product-type-list.component.scss']
})
export class ProductTypeListComponent {
  deleteDialog: any;
  productTypes?: IProductType[];
  deleteId: any;

  constructor(
    private productTypeService: ProductTypeService,
    private router: Router
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getProductTypes();
  }

  getProductTypes() {
    this.productTypeService.getProductTypes().subscribe((res) => {
      if(res && res.body){
        this.productTypes = res.body;
        console.log(this.productTypes)
      }
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
    this.productTypeService.removeProductType(this.deleteId).subscribe((res) => {
      if(res.status == 200){
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
    this.router.navigate(["product-type"], {queryParams: queryParams});
  }
}

