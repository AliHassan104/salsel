import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { BookinglistComponent } from "./list/bookinglist.component";
import { BookingitemComponent } from "./view/bookingitem.component";
import { DaysAgoPipe } from "../Tickets/pipe/days-ago.pipe";
import { ExtractFileNamePipe } from "../Tickets/pipe/extract-file-name.pipe";
import { PadWithZerosPipe } from "../Tickets/pipe/pad-with-zeros.pipe";
import { PartialEmailPipe } from "../Tickets/pipe/partial-email.pipe";
import { SingleCharacterPipe } from "../Tickets/pipe/single-character.pipe";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { AutoCompleteModule } from "primeng/autocomplete";
import { AutoFocusModule } from "primeng/autofocus";
import { AvatarModule } from "primeng/avatar";
import { AvatarGroupModule } from "primeng/avatargroup";
import { ButtonModule } from "primeng/button";
import { CalendarModule } from "primeng/calendar";
import { CascadeSelectModule } from "primeng/cascadeselect";
import { ChipsModule } from "primeng/chips";
import { DialogModule } from "primeng/dialog";
import { DropdownModule } from "primeng/dropdown";
import { ImageModule } from "primeng/image";
import { InputMaskModule } from "primeng/inputmask";
import { InputNumberModule } from "primeng/inputnumber";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { MultiSelectModule } from "primeng/multiselect";
import { PaginatorModule } from "primeng/paginator";
import { ProgressBarModule } from "primeng/progressbar";
import { RadioButtonModule } from "primeng/radiobutton";
import { RatingModule } from "primeng/rating";
import { RippleModule } from "primeng/ripple";
import { SliderModule } from "primeng/slider";
import { TableModule } from "primeng/table";
import { TagModule } from "primeng/tag";
import { ToastModule } from "primeng/toast";
import { ToggleButtonModule } from "primeng/togglebutton";
import { TooltipModule } from "primeng/tooltip";
import { TicketRoutingModule } from "../Tickets/ticket-routing.module";
import { BookingformComponent } from "./update/bookingform.component";
import { BookingRoutingModule } from "./booking-routing.module";
import { TicketsModule } from "../Tickets/tickets.module";
import { CommonPipeModule } from "../common-pipe/common-pipe.module";

@NgModule({
  declarations: [BookinglistComponent, BookingitemComponent],
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
    BookingRoutingModule,
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
    ImageModule,
    CommonPipeModule
  ],
})
export class BookingModule {}
