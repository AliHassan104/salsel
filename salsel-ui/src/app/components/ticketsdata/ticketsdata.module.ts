import { NgModule } from "@angular/core";
import { TableModule } from "primeng/table";
import { CommonModule } from "@angular/common";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { ToggleButtonModule } from "primeng/togglebutton";
import { RippleModule } from "primeng/ripple";
import { MultiSelectModule } from "primeng/multiselect";
import { DropdownModule } from "primeng/dropdown";
import { ProgressBarModule } from "primeng/progressbar";
import { ToastModule } from "primeng/toast";
import { SliderModule } from "primeng/slider";
import { RatingModule } from "primeng/rating";
import { TicketsdataroutingModule } from "./ticketsdatarouting.module";
import { ReactiveFormsModule } from "@angular/forms";
import { TicketsdataComponent } from "./ticketsdata.component";
import { DialogModule } from "primeng/dialog";

@NgModule({
  declarations: [TicketsdataComponent],
  imports: [
    TableModule,
    CommonModule,
    TicketsdataroutingModule,
    ReactiveFormsModule,
    RatingModule,
    ButtonModule,
    SliderModule,
    InputTextModule,
    ToggleButtonModule,
    RippleModule,
    MultiSelectModule,
    DropdownModule,
    ProgressBarModule,
    ToastModule,
    DialogModule,
  ],
})
export class TicketsdataModule {}
