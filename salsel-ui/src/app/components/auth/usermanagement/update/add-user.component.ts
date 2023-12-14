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
import { Password } from "primeng/password";

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
      //   email: new FormControl(null, [Validators.required, Validators.email]),
      name: new FormControl(null, [Validators.required]),
      password: new FormControl(null, [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/),
      ]),
      role: new FormControl(null, Validators.required),
      //   phoneNumber: new FormControl(null, [
      //     Validators.required,
      //     Validators.pattern(/^[\d\s\-\(\)+]{10,}$/),
      //   ]),
    });
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
      this.userService.GetUserById(this.editId).subscribe((res) => {
        this.singleUser = res;
        console.log(this.singleUser);

        this.userForm.setValue({
          name: this.singleUser.name,
          Password: this.singleUser.password,
          role: this.singleUser.roles,
        });
      });
    }
  }

  getRoles() {
    this.rolesService.getRoles().subscribe((res: any) => {
      this.roles = res;
      console.log(this.roles);
    });
  }

  onSubmit(data: IUser) {
    if (this.userForm.valid) {
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
