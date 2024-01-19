import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HistoryComponent } from "./history.component";
import { Route, RouterModule } from "@angular/router";

const routes: Route[] = [{ path: "", component: HistoryComponent }];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HistoryRoutingModule {}
