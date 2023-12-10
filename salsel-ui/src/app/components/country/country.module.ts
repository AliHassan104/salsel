import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { ToastModule } from "primeng/toast";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CountryRoutingModule } from "./country-routing.module";
import { CountryFormComponent } from "./update/country-form.component";
import { DialogModule } from "primeng/dialog";
import { CountryListComponent } from "./list/country-list.component";
import { DropdownModule } from "primeng/dropdown";

@NgModule({
  declarations: [CountryListComponent, CountryFormComponent],
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
  ],
})
export class CountryModule {}
