import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class LoginService {
  _url = environment.URL;

  constructor(private http: HttpClient) {}

  login(data: any) {
    let url = `${this._url}login`;
    return this.http.post(url, data);
  }

  signUp(data: any): Observable<any> {
    let url = `${this._url}signup`;
    return this.http.post<any>(url, data);
  }

  forgotPassword(params: any) {
    return this.http.post<any>(
      `${this._url}forgot-password`,
      {},
      { params, responseType: "text" as "json" }
    );
  }
}
