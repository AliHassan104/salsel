import { NgModule } from "@angular/core";
import { Route, RouterModule, Routes } from "@angular/router";
import { DepartmentCategoryDataComponent } from "./list/department-category-data.component";
import { DepartmentCategoryComponent } from "./update/department-category.component";

const routes: Route[] = [
  { path: "", component: DepartmentCategoryComponent },
  { path: "list", component: DepartmentCategoryDataComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DepartmentCategoryRoutingModule {}
