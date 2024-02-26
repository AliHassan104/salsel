import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VerificationComponent } from './verification.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ToastModule } from 'primeng/toast';



@NgModule({
  declarations: [VerificationComponent],
  imports: [
    CommonModule,
    PasswordModule,
    InputTextModule,
    ReactiveFormsModule,
    ToastModule,
    CheckboxModule,
    ButtonModule,
  ],
})
export class VerificationModule {}
