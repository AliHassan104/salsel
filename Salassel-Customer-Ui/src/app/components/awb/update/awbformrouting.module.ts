import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Route, RouterModule } from "@angular/router";
import { AwbcreationComponent } from "./awbcreation.component";

const routes: Route[] = [{ path: "", component: AwbcreationComponent }];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AwbformroutingModule {}
