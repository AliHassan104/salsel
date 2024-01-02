import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "src/app/components/Tickets/service/formvalidation.service";
import { RolesService } from "../../../permissions/service/roles.service";
import { UserService } from "../service/user.service";
import { IUser } from "../model/userDto";
import { LoginService } from "../../service/login.service";

@Component({
  selector: "app-add-user",
  templateUrl: "./add-user.component.html",
  styleUrls: ["./add-user.component.scss"],
  providers: [MessageService],
})
export class AddUserComponent implements OnInit {
  editMode;
  editId;
  singleUser?: IUser;
  roles?;
  constructor(
    private router: Router,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private formService: FormvalidationService,
    private rolesService: RolesService,
    private userService: UserService,
    private loginService: LoginService
  ) {}

  userForm!: FormGroup;

  ngOnInit(): void {
    this.formSetup();
    this.queryParamSetup();
    this.getRoles();
  }

  formSetup() {
    this.userForm = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.email]),
      firstname: new FormControl(null, [Validators.required]),
      lastname: new FormControl(null, [Validators.required]),
      phone: new FormControl(null, Validators.required),
      password: new FormControl(null, [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/),
      ]),
      roles: new FormControl(null, Validators.required),
      employeeId: new FormControl(null, Validators.required),
    });
  }

  isLowerCaseMissing(): boolean {
    return (
      this.userForm.get("password")?.hasError("pattern") &&
      !/(?=.*[a-z])/.test(this.userForm.value.password)
    );
  }

  isUpperCaseMissing(): boolean {
    return (
      this.userForm.get("password")?.hasError("pattern") &&
      !/(?=.*[A-Z])/.test(this.userForm.value.password)
    );
  }

  isDigitMissing(): boolean {
    return (
      this.userForm.get("password")?.hasError("pattern") &&
      !/(?=.*\d)/.test(this.userForm.value.password)
    );
  }

  isShortPassword(): boolean {
    return (
      this.userForm.get("password")?.hasError("pattern") &&
      !/.{8,}/.test(this.userForm.value.password)
    );
  }

  queryParamSetup() {
    this.route.queryParams.subscribe((params) => {
      // Retrieve editMode and id from the query parameters
      if (params["id"] != null) {
        this.editMode = params["updateMode"] === "true"; // Convert to boolean
        this.editId = +params["id"]; // Convert to number
      } else {
        this.editMode = false;
      }
    });

    this.editForm();
  }

  editForm() {
    if (this.editMode) {
      this.userService.GetUserById(this.editId).subscribe((res: any) => {
        this.singleUser = res;

        this.rolesService.getRoles().subscribe((res: any) => {
          const role = res.filter(
            (value) => value.name == this.singleUser.roles[0].name
          );

          this.userForm.patchValue({
            email: this.singleUser.email,
            firstname: this.singleUser.firstname,
            lastname: this.singleUser.lastname,
            phone: this.singleUser.phone,
            password: this.singleUser.password,
            employeeId: this.singleUser.employeeId,
            roles: role[0],
          });
        });
      });
    }
  }

  getRoles() {
    this.rolesService.getRoles().subscribe((res: any) => {
      this.roles = res;
    });
  }

  onSubmit() {
    if (this.userForm.valid) {
      let formValue = this.userForm.value;
      let fullname = formValue.firstname + " " + formValue.lastname;
      const data = {
        firstname: formValue.firstname,
        lastname: formValue.lastname,
        name: fullname,
        phone: formValue.phone,
        password: formValue.password,
        email: formValue.email,
        employeeId: formValue.employeeId,
        roles: [
          {
            id: formValue.roles.id,
          },
        ],
        status: true,
      };

      if (this.editMode) {
        this.userService.updateUser(this.editId, data).subscribe((res) => {
          this.userForm.reset();
          this.router.navigate(["user/list"]);
        });
      } else {
        this.loginService.signUp(data).subscribe((res) => {
          this.userForm.reset();
          this.router.navigate(["user/list"]);
        });
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.userForm);
    }
  }

  onCancel() {
    this.router.navigate(["user/list"]);
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }
}
