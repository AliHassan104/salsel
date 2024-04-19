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
import { TicketitemComponent } from "./view/ticketitem.component";
import { TicketsdataComponent } from "./list/ticketsdata.component";
import { TicketformComponent } from "./update/ticketform.component";
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
import { TooltipModule } from "primeng/tooltip";
import { DaysAgoPipe } from "./pipe/days-ago.pipe";
import { PartialEmailPipe } from "./pipe/partial-email.pipe";
import { AvatarModule } from "primeng/avatar";
import { AvatarGroupModule } from "primeng/avatargroup";
import { AutoFocusModule } from "primeng/autofocus";
import { TagModule } from "primeng/tag";
import { SingleCharacterPipe } from './pipe/single-character.pipe';
import { ExtractFileNamePipe } from './pipe/extract-file-name.pipe';
import { PadWithZerosPipe } from './pipe/pad-with-zeros.pipe';

@NgModule({
  declarations: [
    TicketitemComponent,
    TicketsdataComponent,
    DaysAgoPipe,
    PartialEmailPipe,
    SingleCharacterPipe,
    ExtractFileNamePipe,
    PadWithZerosPipe,
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
    PaginatorModule,
    TooltipModule,
    AvatarModule,
    AvatarGroupModule,
    AutoFocusModule,
    TagModule,
  ],
})
export class TicketsModule {}
