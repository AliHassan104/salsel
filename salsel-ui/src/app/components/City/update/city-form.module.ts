import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CityFormComponent } from "./city-form.component";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { DialogModule } from "primeng/dialog";
import { DropdownModule } from "primeng/dropdown";
import { InputTextModule } from "primeng/inputtext";
import { TableModule } from "primeng/table";
import { ToastModule } from "primeng/toast";
import { TooltipModule } from "primeng/tooltip";
import { CityFormRoutingModule } from "./city-form-routing.module";

@NgModule({
  declarations: [CityFormComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    DialogModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    ToastModule,
    FormsModule,
    DropdownModule,
    TooltipModule,
    CityFormRoutingModule,
  ],
})
export class CityFormModule {}
