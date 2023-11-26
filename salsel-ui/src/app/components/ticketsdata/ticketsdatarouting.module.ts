import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { TicketsdataComponent } from "./ticketsdata.component";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: "", component: TicketsdataComponent }]),
  ],
  exports: [RouterModule],
})
export class TicketsdataroutingModule {}
