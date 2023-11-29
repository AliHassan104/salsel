import { Component, OnInit } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { AuthService } from "../service/auth.service";
import { Router } from "@angular/router";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})
export class LoginComponent implements OnInit {
  constructor(private _authService: AuthService, private router: Router) {}
  loginForm!: FormGroup;

  ngOnInit(): void {
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
      this._authService.login(value).subscribe((res) => {
        localStorage.setItem("jwt", JSON.stringify(res.jwt));
      });
      console.log(this.loginForm.value);
      this.router.navigate([""]);
      this.loginForm.reset();
    }
  }
}
