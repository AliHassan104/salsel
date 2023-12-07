import { NgModule } from "@angular/core";
import { Route, RouterModule, Routes } from "@angular/router";
import { DepartmentCategoryItemComponent } from "./view/department-category-item.component";
import { DepartmentCategoryDataComponent } from "./list/department-category-data.component";
import { DepartmentCategoryComponent } from "./update/department-category.component";

const routes: Route[] = [
  { path: "", component: DepartmentCategoryDataComponent },
  { path: "form", component: DepartmentCategoryComponent },
  { path: "item", component: DepartmentCategoryItemComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DepartmentCategoryRoutingModule {}
