import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

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

import { DepartmentCategoryRoutingModule } from './department-category-routing.module';
import { DepartmentCategoryItemComponent } from './department-category-item/department-category-item.component';
import { DepartmentCategoryDataComponent } from './department-category-data/department-category-data.component';
import { DepartmentCategoryFormComponent } from './department-category-form/department-category-form.component';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';


@NgModule({
  declarations: [
    DepartmentCategoryItemComponent,
    DepartmentCategoryDataComponent,
    DepartmentCategoryFormComponent
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
    ReactiveFormsModule
  ]
})
export class DepartmentCategoryModule { }
