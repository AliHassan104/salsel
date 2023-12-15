import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CountryFormComponent } from "./country-form.component";
import { CountryFormRoutingModule } from "./country-form-routing.module";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { DialogModule } from "primeng/dialog";
import { DropdownModule } from "primeng/dropdown";
import { InputTextModule } from "primeng/inputtext";
import { TableModule } from "primeng/table";
import { ToastModule } from "primeng/toast";
import { TooltipModule } from "primeng/tooltip";

@NgModule({
  declarations: [CountryFormComponent],
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    ToastModule,
    ReactiveFormsModule,
    FormsModule,
    DialogModule,
    DropdownModule,
    TooltipModule,
    CountryFormRoutingModule,
  ],
})
export class CountryFormModule {}
