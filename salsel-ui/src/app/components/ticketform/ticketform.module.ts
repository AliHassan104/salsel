import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TicketformComponent } from "./ticketform.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TicketformRoutingModule } from "./ticketform_routing.module";
import { AutoCompleteModule } from "primeng/autocomplete";
import { CalendarModule } from "primeng/calendar";
import { CascadeSelectModule } from "primeng/cascadeselect";
import { ChipsModule } from "primeng/chips";
import { DropdownModule } from "primeng/dropdown";
import { InputMaskModule } from "primeng/inputmask";
import { InputNumberModule } from "primeng/inputnumber";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { MultiSelectModule } from "primeng/multiselect";
import { RadioButtonModule } from "primeng/radiobutton";
import { ToastModule } from "primeng/toast";

@NgModule({
  declarations: [TicketformComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TicketformRoutingModule,
    FormsModule,
    AutoCompleteModule,
    CalendarModule,
    ChipsModule,
    DropdownModule,
    InputMaskModule,
    InputNumberModule,
    CascadeSelectModule,
    MultiSelectModule,
    InputTextareaModule,
    InputTextModule,
    RadioButtonModule,
    ToastModule,
  ],
})
export class TicketformModule {}
