import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { ToastModule } from "primeng/toast";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CountryRoutingModule } from "./country-routing.module";
import { DialogModule } from "primeng/dialog";
import { CountryListComponent } from "./list/country-list.component";
import { DropdownModule } from "primeng/dropdown";
import { TooltipModule } from "primeng/tooltip";

@NgModule({
  declarations: [CountryListComponent],
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    ToastModule,
    ReactiveFormsModule,
    FormsModule,
    CountryRoutingModule,
    DialogModule,
    DropdownModule,
    TooltipModule,
  ],
})
export class CountryModule {}
