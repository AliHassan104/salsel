import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { LoginService } from "../service/login.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { MessageService } from "primeng/api";
import { Router } from "@angular/router";
import { ResetPasswordService } from "../new-password/service/reset-password.service";

@Component({
  selector: "app-forgot-password",
  templateUrl: "./forgot-password.component.html",
  styleUrls: ["./forgot-password.component.scss"],
  providers: [MessageService],
})
export class ForgotPasswordComponent implements OnInit {
  visible: boolean = false;
  resetEmail;
  constructor(
    private loginService: LoginService,
    public router: Router,
    private formService: FormvalidationService,
    private messageService: MessageService,
    private resetPassService: ResetPasswordService
  ) {}

  forgotPasswordForm!: FormGroup;
  ngOnInit(): void {
    this.forgotPasswordForm = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.email]),
    });
  }

  formSetup() {}

  onForgotPassword(data: any) {
    const params = { email: data.email };
    if (this.forgotPasswordForm.valid) {
      this.resetPassService.forgotPassword(params).subscribe(
        (res) => {
          this.success(res);
          this.visible = true;
          this.resetEmail = data.email;
          this.forgotPasswordForm.reset();
          setTimeout(() => {
            this.visible = false;
            const queryParams = { email: data.email };
            this.router.navigate(["verification"], {
              queryParams: queryParams,
            });
          }, 1000);
        },
        (error: any) => {
          this.showError(error);
        }
      );
    } else {
      this.formService.markFormGroupTouched(this.forgotPasswordForm);
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Please Enter a Valid Email",
      });
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

  showError(error: any) {
    this.messageService.add({
      severity: "error",
      summary: "Error",
      detail: error?.error?.error,
    });
  }
}
