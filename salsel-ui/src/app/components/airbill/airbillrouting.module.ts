import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AirbilldataComponent } from "./list/airbilldata.component";
import { AwbcreationComponent } from "./update/awbcreation.component";
import { AirbilldetailsComponent } from "./view/airbilldetails.component";
import { Route, RouterModule } from "@angular/router";

const routes: Route[] = [
  { path: "", component: AirbilldataComponent },
  { path: "createairbill", component: AwbcreationComponent },
  { path: ":billid", component: AirbilldetailsComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AirbillroutingModule {}
