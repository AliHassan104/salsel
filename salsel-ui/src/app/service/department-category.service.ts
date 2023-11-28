import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DepartmentCategoryService {

  constructor(private http: HttpClient) {}

  editTicketMode = new BehaviorSubject(false);
  editId = new BehaviorSubject<any>("");

  // url = "http://localhost:8080/api/";

  url = environment.URL + "/department-category";


  // Create Department Category

  save(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}`, data);
  }

  //  Get All Department Category
  getAll() {
    return this.http.get(`${this.url}`);
  }

  //   Get Single Department Category

  getById(id) {
    return this.http.get(`${this.url}/${id}`);
  }

  //   Delete Department Category

  delete(id) {
    return this.http.delete(`${this.url}/${id}`);
  }
  
}
