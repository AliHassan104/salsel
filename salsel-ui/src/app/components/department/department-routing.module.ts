import { NgModule } from "@angular/core";
import { Route, RouterModule, Routes } from "@angular/router";
import { DepartmentComponent } from "./update/department.component";
import { DepartmentListComponent } from "./list/department-list.component";

// const routes: Routes = [];

const routes: Route[] = [
  { path: "", component: DepartmentComponent },
  { path: "list", component: DepartmentListComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DepartmentRoutingModule {}
