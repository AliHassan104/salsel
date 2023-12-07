import { HttpClient } from "@angular/common/http";
import { Component } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { of } from "rxjs";
import { environment } from "src/environments/environment";

@Component({
  selector: "app-permissions",
  templateUrl: "./permissions.component.html",
  styleUrls: ["./permissions.component.scss"],
})
export class PermissionsComponent {
  permissionsForm!: FormGroup;

  list;
  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http
      .get(`${environment.URL}product-field/name/CATEGORIES`)
      .subscribe((res) => {
        this.list = res;
        console.log(this.list);
      });
  }
}
