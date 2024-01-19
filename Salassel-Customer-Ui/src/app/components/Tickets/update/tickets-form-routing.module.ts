import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TicketformComponent } from "./ticketform.component";
import { Route, RouterModule } from "@angular/router";

const routes: Route[] = [
  {
    path: "",
    component: TicketformComponent,
  },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TicketsFormRoutingModule {}
