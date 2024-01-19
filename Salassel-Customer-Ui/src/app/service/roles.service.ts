import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class RolesService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  //   Get All Roles
  getRoles() {
    return this.http.get(`${this.url}role`);
  }

  getPermissions() {
    return this.http.get(`${this.url}permission`);
  }

  getPermissionOfRoles(id: any) {
    return this.http.get(`${this.url}role/${id}`);
  }

  updatePermissionOfRoles(permission: any): Observable<any> {
    return this.http.post(`${this.url}role`, permission);
  }
}
