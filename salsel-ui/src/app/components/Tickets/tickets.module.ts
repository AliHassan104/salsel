import { NgModule } from "@angular/core";
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
import { ReactiveFormsModule } from "@angular/forms";
import { DialogModule } from "primeng/dialog";
import { TicketitemComponent } from "./ticketitem/ticketitem.component";
import { TicketsdataComponent } from "./ticketsdata/ticketsdata.component";
import { TicketformComponent } from "./ticketform/ticketform.component";
import { TableModule } from "primeng/table";
import { TicketRoutingModule } from "./ticket-routing.module";
import { FormsModule } from "@angular/forms";
import { AutoCompleteModule } from "primeng/autocomplete";
import { CalendarModule } from "primeng/calendar";
import { CascadeSelectModule } from "primeng/cascadeselect";
import { ChipsModule } from "primeng/chips";
import { InputMaskModule } from "primeng/inputmask";
import { InputNumberModule } from "primeng/inputnumber";
import { InputTextareaModule } from "primeng/inputtextarea";
import { RadioButtonModule } from "primeng/radiobutton";
import { PaginatorModule } from "primeng/paginator";

@NgModule({
  declarations: [
    TicketitemComponent,
    TicketsdataComponent,
    TicketformComponent,
  ],
  imports: [
    CommonModule,
    TableModule,
    CommonModule,
    ReactiveFormsModule,
    RatingModule,
    ButtonModule,
    SliderModule,
    InputTextModule,
    ToggleButtonModule,
    RippleModule,
    MultiSelectModule,
    ProgressBarModule,
    ToastModule,
    DialogModule,
    TicketRoutingModule,
    FormsModule,
    AutoCompleteModule,
    CalendarModule,
    ChipsModule,
    DropdownModule,
    InputMaskModule,
    InputNumberModule,
    CascadeSelectModule,
    InputTextareaModule,
    RadioButtonModule,
    PaginatorModule
  ],
})
export class TicketsModule {}
