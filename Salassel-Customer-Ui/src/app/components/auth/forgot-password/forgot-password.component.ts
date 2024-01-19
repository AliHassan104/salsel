import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { LoginService } from "../service/login.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { MessageService } from "primeng/api";
import { Router } from "@angular/router";

@Component({
  selector: "app-forgot-password",
  templateUrl: "./forgot-password.component.html",
  styleUrls: ["./forgot-password.component.scss"],
  providers: [MessageService],
})
export class ForgotPasswordComponent implements OnInit {
  constructor(
    private loginService: LoginService,
    public router: Router,
    private formService: FormvalidationService,
    private messageService: MessageService
  ) {}

  forgotPasswordForm!: FormGroup;
  ngOnInit(): void {
    this.forgotPasswordForm = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.email]),
    });
  }

  formSetup() {}

  onForgotPassword(data: any) {
    if (this.forgotPasswordForm.valid) {
      this.loginService.forgotPassword(data).subscribe(
        (res) => {
          this.success(res);
        },
        (error: any) => {
          this.showError();
        }
      );
    } else {
      this.formService.markFormGroupTouched(this.forgotPasswordForm);
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
      detail: "User Not Found",
    });
  }
}
