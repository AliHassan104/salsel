import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Department, IDepartment } from 'src/app/api/department.model';
import { environment } from 'src/environments/environment';

export type EntityDepartmentType = HttpResponse<IDepartment>;
export type EntityDepartmentResponseType = HttpResponse<IDepartment[]>;

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  constructor(private http: HttpClient) {}

  url = environment.URL;

  create(department: IDepartment): Observable<EntityDepartmentType> {
    let url = `${this.url}department`;
    return this.http
      .post<IDepartment>(url, department, { observe: 'response' })
  }

  update(data: any): Observable<any> {
    return this.http.put<any>(`${this.url}`, data);
  }

  getDepartments(): Observable<EntityDepartmentResponseType> {
    let url = `${this.url}department`;
    return this.http
      .get<Department[]>(url, { observe: 'response' })
  }

  getDepartmentById(id?: any): Observable<EntityDepartmentType> {
    let url = `${this.url}department/${id}`;
    return this.http
      .get<Department>(url, { observe: 'response' })
  }
  
  removeDepartment(id: any){
    let url = `${this.url}department/${id}`;
    return this.http
      .delete<Department>(url, { observe: 'response' })
  }
}
