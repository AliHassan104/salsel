import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CityListComponent } from "./city-list/city-list.component";
import { CityFormComponent } from "./city-form/city-form.component";
import { Route, RouterModule } from "@angular/router";

const routes: Route[] = [
  { path: "", component: CityListComponent },
  { path: "addcity", component: CityFormComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CityRoutingModule {}
