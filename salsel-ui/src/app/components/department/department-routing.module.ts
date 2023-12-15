import { NgModule } from "@angular/core";
import { Route, RouterModule, Routes } from "@angular/router";
import { DepartmentListComponent } from "./list/department-list.component";

// const routes: Routes = [];

const routes: Route[] = [{ path: "", component: DepartmentListComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DepartmentRoutingModule {}
