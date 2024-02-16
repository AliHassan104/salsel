import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: "root",
})
export class ResetPasswordService {
  url = environment.URL;

  constructor(private http: HttpClient) {}


  resetPassword(params:any){
    return this.http.post(
      `${this.url}reset-password`,
      {},
      { params,responseType: "text" as "json" }
    );
  }

  forgotPassword(params:any){
    return this.http.post(
      `${this.url}forgot-password`,
      {},
      { params, responseType: "text" as "json" }
    );
  }
}
