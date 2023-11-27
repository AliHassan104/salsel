import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AwbcreationComponent } from "./awbcreation.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
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
import { AwbcreationroutingModule } from "./awbcreationrouting.module";

@NgModule({
  declarations: [AwbcreationComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
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
    AwbcreationroutingModule,
  ],
})
export class AwbcreationModule {}
