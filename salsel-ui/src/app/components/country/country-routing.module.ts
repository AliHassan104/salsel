import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CountryListComponent } from "./country-list/country-list.component";
import { CountryFormComponent } from "./country-form/country-form.component";
import { Route, RouterModule } from "@angular/router";

const routes: Route[] = [
  { path: "", component: CountryListComponent },
  { path: "addcountry", component: CountryFormComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CountryRoutingModule {}
