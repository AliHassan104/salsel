import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { ToastModule } from "primeng/toast";
import { NewPasswordComponent } from "./new-password.component";
import { PasswordModule } from "primeng/password";

@NgModule({
  declarations: [NewPasswordComponent],
  imports: [
    CommonModule,
    InputTextModule,
    ButtonModule,
    ReactiveFormsModule,
    FormsModule,
    ToastModule,
    PasswordModule,
  ],
})
export class NewPasswordModule {}
