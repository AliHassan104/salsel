import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { ToastModule } from "primeng/toast";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { DialogModule } from "primeng/dialog";
import { CityFormComponent } from "./city-form/city-form.component";
import { CityRoutingModule } from "./city-routing.module";
import { CityListComponent } from "./city-list/city-list.component";
import { DropdownModule } from "primeng/dropdown";

@NgModule({
  declarations: [CityFormComponent, CityListComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CityRoutingModule,
    DialogModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    ToastModule,
    FormsModule,
    DropdownModule,
  ],
})
export class CityModule {}