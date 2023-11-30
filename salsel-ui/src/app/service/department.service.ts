import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class DepartmentService {
  constructor(private http: HttpClient) {}

  editTicketMode = new BehaviorSubject(false);
  editId = new BehaviorSubject<any>("");

  // url = "http://localhost:8080/api/";

  url = environment.URL + "department";

  // Create Ticket

  save(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}`, data);
  }
  // Create Ticket

  update(data: any): Observable<any> {
    return this.http.put<any>(`${this.url}`, data);
  }

  //  Get All Tickets
  getAll() {
    return this.http.get(`${this.url}`);
  }

  //   Get Single Ticket

  getById(id) {
    return this.http.get(`${this.url}/${id}`);
  }

  //   Delete Ticket

  delete(id) {
    return this.http.delete(`${this.url}/${id}`);
  }
}
