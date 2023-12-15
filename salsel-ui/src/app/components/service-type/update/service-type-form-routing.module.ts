import { NgModule } from "@angular/core";
import { Route, RouterModule } from "@angular/router";
import { ServiceTypeComponent } from "./service-type.component";

const routes: Route[] = [{ path: "", component: ServiceTypeComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ServiceTypeFormRoutingModule {}
