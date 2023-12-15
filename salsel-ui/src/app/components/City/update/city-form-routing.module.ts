import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Route, RouterModule } from "@angular/router";
import { CityFormComponent } from "./city-form.component";

const routes: Route[] = [{ path: "", component: CityFormComponent }];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CityFormRoutingModule {}
