import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CityListComponent } from "./list/city-list.component";
import { Route, RouterModule } from "@angular/router";

const routes: Route[] = [{ path: "", component: CityListComponent }];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CityRoutingModule {}
