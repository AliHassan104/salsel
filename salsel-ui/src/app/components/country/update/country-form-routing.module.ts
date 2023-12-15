import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Route, RouterModule } from "@angular/router";
import { CountryFormComponent } from "./country-form.component";

const routes: Route[] = [{ path: "", component: CountryFormComponent }];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CountryFormRoutingModule {}
