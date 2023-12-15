import { Component, OnInit } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { AuthService } from "../service/auth.service";
import { Router } from "@angular/router";
import { LoginService } from "../service/login.service";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { UserService } from "../usermanagement/service/user.service";
import { SessionStorageService } from "../service/session-storage.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
  providers: [MessageService],
})
export class LoginComponent implements OnInit {
  constructor(
    private _loginService: LoginService,
    private router: Router,
    private messageService: MessageService,
    private formService: FormvalidationService,
    private userService: UserService,
    private SessionStorageService: SessionStorageService
  ) {}
  loginForm!: FormGroup;
  token;
  ngOnInit(): void {
    localStorage.removeItem("token");

    this.loginForm = new FormGroup({
      name: new FormControl(null, Validators.required),
      password: new FormControl(null, [Validators.required]),
    });
  }

  //   alreadyExistUser(control: FormControl): { [s: string]: boolean } {
  //     if (this.userDetailList.indexOf(control.value) !== -1) {
  //       return { userAlreadyExist: true };
  //     }
  //     return null as any;
  //   }

  onLogin(value) {
    if (this.loginForm.valid) {
      this._loginService.login(value).subscribe(
        (res) => {
          this.token = res;
          localStorage.setItem("token", this.token.jwt);
          this.router.navigate([""]);
          this.loginForm.reset();
          this.userService.getUserByName(value.name).subscribe((res: any) => {
            localStorage.setItem("loginUserName", res.name);
          });
        },
        (error) => {
          this.showError(error);
        }
      );
    } else {
      this.messageService.add({
        severity: "warn",
        summary: "Error",
        detail: "Please fill all fields",
      });
      this.formService.markFormGroupTouched(this.loginForm);
    }
  }

  showError(error: any) {
    this.messageService.add({
      severity: "error",
      summary: "Error",
      detail: error.error.error,
    });
  }
}
