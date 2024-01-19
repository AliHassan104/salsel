import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { LoginComponent } from "./login.component";
import { PasswordModule } from "primeng/password";
import { InputTextModule } from "primeng/inputtext";
import { ReactiveFormsModule } from "@angular/forms";
import { ToastModule } from "primeng/toast";

@NgModule({
  declarations: [LoginComponent],
  imports: [
    CommonModule,
    PasswordModule,
    InputTextModule,
    ReactiveFormsModule,
    ToastModule,
  ],
})
export class LoginModule {}
