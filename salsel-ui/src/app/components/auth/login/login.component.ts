import { Component, OnInit } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      user: new FormControl(null, Validators.required),
      password: new FormControl(null, [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/),
      ]),
    });
  }

  //   alreadyExistUser(control: FormControl): { [s: string]: boolean } {
  //     if (this.userDetailList.indexOf(control.value) !== -1) {
  //       return { userAlreadyExist: true };
  //     }
  //     return null as any;
  //   }

  onLogin() {
    if (this.loginForm.valid) {
      console.log(this.loginForm.value);
      this.loginForm.reset();
    }
  }
}
