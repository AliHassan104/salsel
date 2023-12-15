import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { DepartmentComponent } from "./department.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AutoCompleteModule } from "primeng/autocomplete";
import { CalendarModule } from "primeng/calendar";
import { CascadeSelectModule } from "primeng/cascadeselect";
import { ChipsModule } from "primeng/chips";
import { DialogModule } from "primeng/dialog";
import { DropdownModule } from "primeng/dropdown";
import { InputMaskModule } from "primeng/inputmask";
import { InputNumberModule } from "primeng/inputnumber";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { MultiSelectModule } from "primeng/multiselect";
import { TableModule } from "primeng/table";
import { ToastModule } from "primeng/toast";
import { TooltipModule } from "primeng/tooltip";
import { DepartmentFormRoutingModule } from "./department-form-routing.module";

@NgModule({
  declarations: [DepartmentComponent],
  imports: [
    CommonModule,
    TableModule,
    DialogModule,
    AutoCompleteModule,
    CalendarModule,
    CascadeSelectModule,
    ChipsModule,
    DropdownModule,
    InputMaskModule,
    InputNumberModule,
    InputTextModule,
    InputTextareaModule,
    MultiSelectModule,
    FormsModule,
    ReactiveFormsModule,
    ToastModule,
    TooltipModule,
    DepartmentFormRoutingModule,
  ],
})
export class DepartmentFormModule {}
