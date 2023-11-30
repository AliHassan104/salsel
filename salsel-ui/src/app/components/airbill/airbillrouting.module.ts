import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AirbilldataComponent } from "./airbilldata/airbilldata.component";
import { AwbcreationComponent } from "./awbcreation/awbcreation.component";
import { AirbilldetailsComponent } from "./airbilldetails/airbilldetails.component";
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
