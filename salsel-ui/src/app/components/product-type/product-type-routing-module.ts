import { NgModule } from "@angular/core";
import { Route, RouterModule } from "@angular/router";
import { ProductTypeListComponent } from "./list/product-type-list.component";

const routes: Route[] = [{ path: "", component: ProductTypeListComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProductTypeRoutingModule {}
