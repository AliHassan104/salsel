import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { ToastModule } from "primeng/toast";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CountryRoutingModule } from "./country-routing.module";
import { CountryFormComponent } from "./country-form/country-form.component";
import { DialogModule } from "primeng/dialog";
import { CountryListComponent } from "./country-list/country-list.component";

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
  ],
})
export class CountryModule {}
