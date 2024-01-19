import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Department, IDepartment } from "src/app/Model/department.model";
import { environment } from "src/environments/environment";

export type EntityDepartmentType = HttpResponse<IDepartment>;
export type EntityDepartmentResponseType = HttpResponse<IDepartment[]>;

@Injectable({
  providedIn: "root",
})
export class DepartmentService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  getDepartments(params: any): Observable<EntityDepartmentResponseType> {
    let url = `${this.url}department`;
    return this.http.get<Department[]>(url, { params, observe: "response" });
  }

  getDepartmentById(id?: any): Observable<EntityDepartmentType> {
    let url = `${this.url}department/${id}`;
    return this.http.get<Department>(url, { observe: "response" });
  }
}
