import { HttpClient } from "@angular/common/http";
import { Component } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { MessageService } from "primeng/api";
import { Ticket } from "src/app/api/ticket";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";
import { environment } from "src/environments/environment";

@Component({
  selector: "app-add-user",
  templateUrl: "./add-user.component.html",
  styleUrls: ["./add-user.component.scss"],
  providers: [MessageService],
})
export class AddUserComponent {
  editMode;
  editId;
  singleUser;
  constructor(
    private router: Router,
    private http: HttpClient,
    private messageService: MessageService,
    private route: ActivatedRoute
  ) {}

  userForm!: FormGroup;

  roles: any = ["ROLE_ADMIN", "ROLE_USER"];

  ngOnInit(): void {
    // Reactive Form
    this.userForm = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.email]),
      name: new FormControl(null, [Validators.required]),
      password: new FormControl(null, [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/),
      ]),
      phoneNumber: new FormControl(null, [
        Validators.required,
        Validators.pattern(/^[\d\s\-\(\)+]{10,}$/),
      ]),
    });

    this.route.queryParams.subscribe((params) => {
      // Retrieve editMode and id from the query parameters
      if (params["id"] != null) {
        this.editMode = params["updateMode"] === "true"; // Convert to boolean
        this.editId = +params["id"]; // Convert to number
      } else {
        this.editMode = false;
      }
    });
  }

  onSubmit() {}

  onCancel() {
    this.router.navigate(["userslist"]);
  }

  //   Pop up message
  success() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket added succesfully",
    });
  }

  update() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket Updated succesfully",
    });
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }

  ngOnDestroy(): void {}

  // Function to mark all controls in a FormGroup as touched
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach((control) => {
      control.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
}
