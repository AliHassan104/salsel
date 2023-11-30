import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
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
import { ProductComponent } from './product/product.component';
import { ProductListComponent } from './list/product-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProductFieldRoutingModule } from './product-field-routing-module';
import { ToastModule } from "primeng/toast";

@NgModule({
  declarations: [
    ProductComponent,
    ProductListComponent
  ],
  imports: [
    CommonModule,
    ProductFieldRoutingModule,
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
  ]
})
export class ProductFieldModule { }
