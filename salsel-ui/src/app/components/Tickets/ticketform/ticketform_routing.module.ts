import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { TicketformComponent } from "./ticketform.component";

@NgModule({
  imports: [
    RouterModule.forChild([{ path: "", component: TicketformComponent }]),
  ],
  exports: [RouterModule],
})
export class TicketformRoutingModule {}
