import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class AuthService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  login(data: any): Observable<any> {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.post<any>(`${this.url}login`, data, {
      headers,
    });
  }

  idLoggedIn() {
    return sessionStorage.getItem("token") != null;
  }

  getToken() {
    return sessionStorage.getItem("token") || "";
  }
}
