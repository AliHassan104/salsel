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
import { ActivatedRoute, Router } from "@angular/router";
import { ResetPasswordService } from "./service/reset-password.service";

@Component({
  selector: "app-new-password",
  templateUrl: "./new-password.component.html",
  styleUrls: ["./new-password.component.scss"],
  providers: [MessageService],
})
export class NewPasswordComponent {
    userMail;
    resetCode
  constructor(
    private loginService: LoginService,
    public router: Router,
    private formService: FormvalidationService,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private resetpassService: ResetPasswordService
  ) {}

  newPasswordForm!: FormGroup;
  ngOnInit(): void {
    this.formSetup();
    this.queryParamSetup()
  }

  queryParamSetup() {
    this.route.queryParams.subscribe((params: any) => {
      console.log(params);
      this.userMail = params?.email
      this.resetCode = params?.code;

    });
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
        const params={
            email: this.userMail,
            code: this.resetCode,
            newPassword : data.password
        }
      this.resetpassService.resetPassword(params).subscribe(
        (res: any) => {
          this.success(res);
          this.newPasswordForm.reset()
        },
        (error: any) => {
             this.messageService.add({
               severity: "error",
               summary: "Error",
               detail: "Invalid or expired reset code"
             });
        }
      );
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
      detail: "Fill all the required fields.",
    });
  }
}
