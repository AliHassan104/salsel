import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AwbcreationComponent } from "./update/awbcreation.component";
import { AirbilldetailsComponent } from "./view/airbilldetails.component";
import { AirbilldataComponent } from "./list/airbilldata.component";
import { TableModule } from "primeng/table";
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
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { DialogModule } from "primeng/dialog";
import { AutoCompleteModule } from "primeng/autocomplete";
import { CalendarModule } from "primeng/calendar";
import { CascadeSelectModule } from "primeng/cascadeselect";
import { ChipsModule } from "primeng/chips";
import { InputMaskModule } from "primeng/inputmask";
import { InputNumberModule } from "primeng/inputnumber";
import { InputTextareaModule } from "primeng/inputtextarea";
import { AirbillroutingModule } from "./airbillrouting.module";
import { RadioButtonModule } from "primeng/radiobutton";
import { TooltipModule } from "primeng/tooltip";

@NgModule({
  declarations: [
    AwbcreationComponent,
    AirbilldetailsComponent,
    AirbilldataComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
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
    AutoCompleteModule,
    CalendarModule,
    ChipsModule,
    InputMaskModule,
    InputNumberModule,
    CascadeSelectModule,
    InputTextareaModule,
    AirbillroutingModule,
    RadioButtonModule,
    TooltipModule,
  ],
})
export class AirbillModule {}
