import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

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

import { DepartmentCategoryRoutingModule } from "./department-category-routing.module";
import { DepartmentCategoryItemComponent } from "./view/department-category-item.component";
import { TableModule } from "primeng/table";
import { DialogModule } from "primeng/dialog";
import { DepartmentCategoryDataComponent } from "./list/department-category-data.component";
import { DepartmentCategoryComponent } from "./update/department-category.component";

@NgModule({
  declarations: [
    DepartmentCategoryItemComponent,
    DepartmentCategoryDataComponent,
    DepartmentCategoryComponent,
  ],
  imports: [
    CommonModule,
    DepartmentCategoryRoutingModule,
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
  ],
})
export class DepartmentCategoryModule {}
