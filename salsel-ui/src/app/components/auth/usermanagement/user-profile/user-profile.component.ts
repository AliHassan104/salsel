import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { UserService } from "../service/user.service";
import { IUser } from "../model/userDto";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { Password } from "primeng/password";
import { MessageService } from "primeng/api";

@Component({
  selector: "app-user-profile",
  templateUrl: "./user-profile.component.html",
  styleUrls: ["./user-profile.component.scss"],
  providers: [MessageService],
})
export class UserProfileComponent implements OnInit {
  userEmail?;
  activeUser?: IUser;
  editMode?: boolean = false;
  visible: boolean = false;
  userForm!: FormGroup;
  newPasswordForm!: FormGroup;

  @ViewChild("inputField") inputField: ElementRef;

  constructor(
    private userService: UserService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.userForm = new FormGroup({
      firstname: new FormControl(null, Validators.required),
      lastname: new FormControl(null, Validators.required),
      email: new FormControl(null, Validators.required),
      phone: new FormControl(null, Validators.required),
      employeeId: new FormControl(null, Validators.required),
      role: new FormControl(null, Validators.required),
    });

    this.newPasswordForm = new FormGroup({
      currentPassword: new FormControl(null, Validators.required),
      password: new FormControl(null, Validators.required),
      confirmPassword: new FormControl(null, Validators.required),
    });

    this.userEmail = localStorage.getItem("loginUserEmail");

    this.getActiveUser();
  }

  patchFormVlues() {
    this.userForm.disable();

    this.userForm.setValue({
      firstname: this.activeUser?.firstname,
      lastname: this.activeUser?.lastname,
      email: this.activeUser?.email,
      phone: this.activeUser?.phone,
      employeeId: this.activeUser?.employeeId,
      role: this.activeUser?.roles[0].name,
    });
  }

  getActiveUser() {
    this.userService.getUserByEmail(this.userEmail).subscribe((res: any) => {
      this.activeUser = res;

      this.patchFormVlues();
      console.log(this.activeUser);
    });
  }

  updateInfo() {
    this.userForm.enable();
    this.inputField.nativeElement.focus();
    this.userForm.get("role").disable();
    this.userForm.get("employeeId").disable();
    this.editMode = true;
  }

  onCancel() {
    this.editMode = false;
    this.userForm.disable();
  }

  onSubmit(data: IUser) {
    if (this.userForm.valid) {
      let name = data.firstname + " " + data.lastname;
      const userData = {
        firstname: data.firstname,
        lastname: data.lastname,
        email: data.email,
        phone: data.phone,
        name: name,
        roles: [
          {
            id: this.activeUser?.roles[0].id,
          },
        ],
        employeeId: this.activeUser.employeeId,
        password: "Qwerty.@12",
        status: true,
      };
      this.userService
        .updateUser(this.activeUser.id, userData)
        .subscribe((res) => {
          this.editMode = false;
          this.getActiveUser();
          this.userService.loginUserName.next(name);
          this.userService.loginUser.next(name.charAt(0).toUpperCase());
          this.userService.loginUserEmail.next(data.email);
          localStorage.removeItem("loginUserName");
          localStorage.removeItem("loginUserEmail");
          localStorage.setItem("loginUserName", name);
          localStorage.setItem("loginUserEmail", data.email);
          this.success();
        });
    } else {
      this.messageService.add({
        severity: "error",
        summary: "Warning",
        detail: "Please ensure that all required details are filled out.",
      });
    }
  }

  success() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Information Updated Successfully",
    });
  }

  onChangePass() {}

  showDialog() {
    this.visible = true;
  }

  closeDialog() {
    this.visible = false;
  }

  navigateToForgotPassword() {
    this.router.navigate(["/forgot-password"]);
  }
}
