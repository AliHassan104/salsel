import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { DialogModule } from "primeng/dialog";
import { TicketitemComponent } from "./ticketitem.component";
import { TicketitemRoutingModule } from "./ticketitem-routing.module";
import { ButtonModule } from "primeng/button";
@NgModule({
  declarations: [TicketitemComponent],
  imports: [CommonModule, DialogModule, TicketitemRoutingModule, ButtonModule],
})
export class TicketitemModule {}
