import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { UserService } from "../service/user.service";
import { IUser } from "../model/userDto";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from "@angular/forms";
import { Router } from "@angular/router";
import { Password } from "primeng/password";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "src/app/components/Tickets/service/formvalidation.service";

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
    private messageService: MessageService,
    private formService: FormvalidationService
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

    this.newPasswordForm = new FormGroup(
      {
        currentPassword: new FormControl(null, Validators.required),
        password: new FormControl(null, Validators.required),
        confirmPassword: new FormControl(null, Validators.required),
      },
      { validators: this.matchPassword }
    );

    this.userEmail = sessionStorage.getItem("loginUserEmail");

    this.getActiveUser();
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

  patchFormVlues() {
    this.userForm.disable();

    this.userForm.setValue({
      firstname: this.activeUser?.firstname,
      lastname: this.activeUser?.lastname,
      email: this.activeUser?.email,
      phone: this.activeUser?.phone,
      employeeId: this.activeUser?.employeeId,
      role: this.activeUser?.roles[0]?.name,
    });
  }

  getActiveUser() {
    this.userService.getUserByEmail(this.userEmail).subscribe((res: any) => {
      this.activeUser = res;

      this.patchFormVlues();
    });
  }

  updateInfo() {
    this.userForm.enable();
    this.inputField.nativeElement.focus();
    this.userForm.get("role").disable();
    this.userForm.get("employeeId").disable();
    this.userForm.get("email").disable();
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
        email: this.activeUser.email,
        phone: data.phone,
        name: name,
        roles: [
          {
            id: this.activeUser?.roles[0]?.id,
          },
        ],
        employeeId: this.activeUser.employeeId,
        status: true,
      };
      this.userService.updateUser(this.activeUser.id, userData).subscribe(
        (res: any) => {
          this.editMode = false;
          this.userService.loginUserName.next(name);
          this.userService.loginUser.next(name.charAt(0).toUpperCase());
          this.userService.loginUserEmail.next(this.activeUser.email);
          sessionStorage.removeItem("loginUserName");
          sessionStorage.setItem("loginUserName", name);
          this.getActiveUser();
          this.success();
        },
        (error: any) => {
          this.messageService.add({
            severity: "error",
            summary: "Error",
            detail: "Error Updating User",
          });
        }
      );
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

  onChangePass(data: any) {
    if (this.newPasswordForm.valid) {
      const params = {
        currentPassword: data.currentPassword,
        newPassword: data.password,
      };

      this.userService.changePassword(this.activeUser.id, params).subscribe(
        (res: any) => {
          if (res == "Current password is incorrect") {
            this.messageService.add({
              severity: "error",
              summary: "Error",
              detail: res,
            });
          } else {
            this.messageService.add({
              severity: "success",
              summary: "Success",
              detail: "Password Updated Successfully",
            });
            this.newPasswordForm.reset();
            this.visible = false;
          }
        },
        (error: any) => {
          this.messageService.add({
            severity: "error",
            summary: "Error",
            detail: error.error.error,
          });
        }
      );
    } else {
      this.formService.markFormGroupTouched(this.newPasswordForm);
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Fill in all the required information",
      });
    }
  }

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
