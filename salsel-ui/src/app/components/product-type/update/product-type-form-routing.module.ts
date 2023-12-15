import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ProductTypeComponent } from "./product-type.component";
import { Route, RouterModule } from "@angular/router";

const routes: Route[] = [{ path: "", component: ProductTypeComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProductTypeFormRoutingModule {}
