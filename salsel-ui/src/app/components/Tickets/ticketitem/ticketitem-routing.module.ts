import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { TicketitemComponent } from "./ticketitem.component";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: "", component: TicketitemComponent }]),
  ],
})
export class TicketitemRoutingModule {}
