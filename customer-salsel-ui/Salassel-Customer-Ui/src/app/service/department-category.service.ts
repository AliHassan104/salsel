import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import {
  DepartmentCategory,
  IDepartmentCategory,
} from "src/app/Model/department-category.model";
import { environment } from "src/environments/environment";

export type EntityDepartmentCategoryType = HttpResponse<IDepartmentCategory>;
export type EntityDepartmentCategoryResponseType = HttpResponse<
  IDepartmentCategory[]
>;

@Injectable({
  providedIn: "root",
})
export class DepartmentCategoryService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  getDepartmentCategories(
    params: any
  ): Observable<EntityDepartmentCategoryResponseType> {
    let url = `${this.url}department-category`;
    return this.http.get<DepartmentCategory[]>(url, {
      params,
      observe: "response",
    });
  }

  getDepartmentCategoryById(
    id?: any
  ): Observable<EntityDepartmentCategoryType> {
    let url = `${this.url}department-category/${id}`;
    return this.http.get<DepartmentCategory>(url, { observe: "response" });
  }
}
