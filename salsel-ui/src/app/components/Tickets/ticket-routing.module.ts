import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Route, RouterModule } from "@angular/router";
import { TicketsdataComponent } from "./ticketsdata/ticketsdata.component";
import { TicketformComponent } from "./ticketform/ticketform.component";
import { TicketitemComponent } from "./ticketitem/ticketitem.component";

const routes: Route[] = [
  { path: "", component: TicketsdataComponent },
  { path: "createticket", component: TicketformComponent },
  { path: ":id", component: TicketitemComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TicketRoutingModule {}
