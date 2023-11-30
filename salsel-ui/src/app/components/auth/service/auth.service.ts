import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.post<any>(`http://localhost:8080/api/login`, data, {
      headers,
    });
  }

  idLoggedIn() {
    return localStorage.getItem("token") != null;
  }

  getToken() {
    return localStorage.getItem("token") || "";
  }
}
