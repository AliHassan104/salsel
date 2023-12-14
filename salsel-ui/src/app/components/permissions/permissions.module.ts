import { NgModule, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { PermissionsComponent } from "./permissions.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ToastModule } from "primeng/toast";
import { DropdownModule } from "primeng/dropdown";
import { ButtonModule } from "primeng/button";
import { CheckboxModule } from "primeng/checkbox";
import { InputTextModule } from "primeng/inputtext";

@NgModule({
  declarations: [PermissionsComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ToastModule,
    DropdownModule,
    ButtonModule,
    CheckboxModule,
    FormsModule,
    InputTextModule,
  ],
})
export class PermissionsModule {}
