import { Component, OnInit } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { AuthService } from "../service/auth.service";
import { Router } from "@angular/router";
import { LoginService } from "../service/login.service";
import { MessageService } from "primeng/api";

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
    private messageService: MessageService
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
    this._loginService.login(value).subscribe(
      (res) => {
        this.token = res;
        localStorage.setItem("token", this.token.jwt);
        console.log(this.loginForm.value);
        this.router.navigate([""]);
        this.loginForm.reset();
      },
      (error) => {
        console.log(error);
        this.showError(error);
      }
    );
  }

  showError(error: any) {
    this.messageService.add({
      severity: "error",
      summary: "Error",
      detail: error.error.error,
    });
  }
}
