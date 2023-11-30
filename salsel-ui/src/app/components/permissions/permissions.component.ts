import { Component } from "@angular/core";
import { FormGroup } from "@angular/forms";

@Component({
  selector: "app-permissions",
  templateUrl: "./permissions.component.html",
  styleUrls: ["./permissions.component.scss"],
})
export class PermissionsComponent {
  permissionsForm!: FormGroup;
}
