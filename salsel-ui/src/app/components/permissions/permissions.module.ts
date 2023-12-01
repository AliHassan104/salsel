import { NgModule, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { PermissionsComponent } from "./permissions.component";
import { ReactiveFormsModule } from "@angular/forms";

@NgModule({
  declarations: [PermissionsComponent],
  imports: [CommonModule, ReactiveFormsModule],
})
export class PermissionsModule {}
