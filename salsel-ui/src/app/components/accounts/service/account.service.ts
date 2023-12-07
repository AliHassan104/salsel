import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class AccountService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  //   GET ALL ACCOUNTS

  getAllAccounts() {
    return this.http.get(`${this.url}account`);
  }

  // ADD ACCOUNT

  addAccount(data: any): Observable<any> {
    return this.http.post(`${this.url}account`, data);
  }

  // DELETE ACCOUNT

  deleteAccount(id: any) {
    return this.http.delete(`${this.url}account/${id}`);
  }

  // EDIT ACCOUNT
  editAccount(id: any, data: any) {
    return this.http.put(`${this.url}account/${id}`, data);
  }

  //   GET SINGLE ACCOUNT
  getSingleAccount(id: any) {
    return this.http.get(`${this.url}account/${id}`);
  }
}
