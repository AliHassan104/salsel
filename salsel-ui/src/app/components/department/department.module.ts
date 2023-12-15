import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TableModule } from "primeng/table";
import { DialogModule } from "primeng/dialog";

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

import { FormsModule, ReactiveFormsModule } from "@angular/forms";

import { DepartmentRoutingModule } from "./department-routing.module";
import { DepartmentListComponent } from "./list/department-list.component";
import { ToastModule } from "primeng/toast";
import { TooltipModule } from "primeng/tooltip";

@NgModule({
  declarations: [DepartmentListComponent],
  imports: [
    CommonModule,
    DepartmentRoutingModule,
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
  ],
})
export class DepartmentModule {}
