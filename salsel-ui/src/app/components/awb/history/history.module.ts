import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TimelineModule } from "primeng/timeline";
import { HistoryComponent } from "./history.component";
import { HistoryRoutingModule } from "./history-routing.module";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { CalendarModule } from "primeng/calendar";
import { DividerModule } from "primeng/divider";
@NgModule({
  declarations: [HistoryComponent],
  imports: [
    CommonModule,
    TimelineModule,
    HistoryRoutingModule,
    ButtonModule,
    CardModule,
    CalendarModule,
    DividerModule,
  ],
})
export class HistoryModule {}
