import { Component } from "@angular/core";
import {
  FormGroup,
  FormControl,
  Validators,
  AbstractControl,
  ValidatorFn,
  ValidationErrors,
} from "@angular/forms";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { LoginService } from "../service/login.service";
import { Router } from "@angular/router";

@Component({
  selector: "app-new-password",
  templateUrl: "./new-password.component.html",
  styleUrls: ["./new-password.component.scss"],
  providers: [MessageService],
})
export class NewPasswordComponent {
  constructor(
    private loginService: LoginService,
    public router: Router,
    private formService: FormvalidationService,
    private messageService: MessageService
  ) {}

  newPasswordForm!: FormGroup;
  ngOnInit(): void {
    this.formSetup();
  }

  formSetup() {
    this.newPasswordForm = new FormGroup(
      {
        password: new FormControl(null, [Validators.required]),
        confirmPassword: new FormControl(null, [Validators.required]),
      },
      { validators: this.matchPassword }
    );
  }

  matchPassword: ValidatorFn = (
    control: AbstractControl
  ): ValidationErrors | null => {
    let password = control.get("password");
    let confirmPassword = control.get("confirmPassword");
    if (
      password &&
      confirmPassword &&
      password?.value != confirmPassword.value
    ) {
      return { passwordMatchError: true };
    }
    return null;
  };

  onResetPassword(data: any) {
    console.log(this.newPasswordForm);

    if (this.newPasswordForm.valid) {
      console.log(data);
    } else {
      this.formService.markFormGroupTouched(this.newPasswordForm);
      this.showError();
    }
  }

  onCancel() {
    this.router.navigate(["/login"]);
  }

  success(res: any) {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: res,
    });
  }

  showError() {
    this.messageService.add({
      severity: "error",
      summary: "Error",
      detail: "Password Not Matched",
    });
  }
}
