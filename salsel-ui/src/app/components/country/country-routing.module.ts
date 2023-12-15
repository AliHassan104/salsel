import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CountryListComponent } from "./list/country-list.component";
import { CountryFormComponent } from "./update/country-form.component";
import { Route, RouterModule } from "@angular/router";

const routes: Route[] = [{ path: "", component: CountryListComponent }];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CountryRoutingModule {}
