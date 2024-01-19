import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Route, RouterModule } from "@angular/router";
import { TicketsdataComponent } from "./list/ticketsdata.component";
import { TicketitemComponent } from "./view/ticketitem.component";

const routes: Route[] = [
  { path: "", component: TicketsdataComponent },
  { path: ":id", component: TicketitemComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TicketRoutingModule {}
