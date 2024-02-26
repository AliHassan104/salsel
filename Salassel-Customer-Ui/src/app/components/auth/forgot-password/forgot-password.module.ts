import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ForgotPasswordComponent } from "./forgot-password.component";
import { InputTextModule } from "primeng/inputtext";
import { ButtonModule } from "primeng/button";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ToastModule } from "primeng/toast";
import { DialogModule } from "primeng/dialog";
import { PasswordModule } from "primeng/password";
import { HideEmailPipe } from './hide-email.pipe';

@NgModule({
  declarations: [ForgotPasswordComponent, HideEmailPipe],
  imports: [
    CommonModule,
    InputTextModule,
    ButtonModule,
    ReactiveFormsModule,
    FormsModule,
    ToastModule,
    DialogModule,
    PasswordModule,
  ],
})
export class ForgotPasswordModule {}
