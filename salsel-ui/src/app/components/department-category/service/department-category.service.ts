import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import {
  DepartmentCategory,
  IDepartmentCategory,
} from "src/app/components/department-category/model/department-category.model";
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

  create(
    departmentCategory: IDepartmentCategory
  ): Observable<EntityDepartmentCategoryType> {
    let url = `${this.url}department-category`;
    return this.http.post<IDepartmentCategory>(url, departmentCategory, {
      observe: "response",
    });
  }

  update(data: any): Observable<any> {
    return this.http.put<any>(`${this.url}`, data);
  }

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

  removeDepartmentCategory(id: any) {
    let url = `${this.url}department-category/${id}`;
    return this.http.delete<DepartmentCategory>(url, { observe: "response" });
  }

  updateDepartmentCategoryStatus(id: any) {
    let url = `${this.url}department-category/status/${id}`;
    return this.http.put<DepartmentCategory>(url, { observe: "response" });
  }
}
