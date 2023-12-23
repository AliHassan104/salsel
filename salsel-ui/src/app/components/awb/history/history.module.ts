import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TimelineModule } from "primeng/timeline";
import { HistoryComponent } from "./history.component";
import { HistoryRoutingModule } from "./history-routing.module";

@NgModule({
  declarations: [HistoryComponent],
  imports: [CommonModule, TimelineModule, HistoryRoutingModule],
})
export class HistoryModule {}
