import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
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
}
