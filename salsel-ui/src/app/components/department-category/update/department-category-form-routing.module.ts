import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Route, RouterModule } from "@angular/router";
import { DepartmentCategoryComponent } from "./department-category.component";

const routes: Route[] = [{ path: "", component: DepartmentCategoryComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DepartmentCategoryFormRoutingModule {}
