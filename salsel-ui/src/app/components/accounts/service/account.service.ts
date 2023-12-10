import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { IAccountData } from "src/app/components/accounts/model/accountValuesDto";
import { environment } from "src/environments/environment";

export type EntityAccountType = HttpResponse<IAccountData>;
export type EntityAccountFieldResponseType = HttpResponse<IAccountData[]>;
@Injectable({
  providedIn: "root",
})
export class AccountService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  //   GET ALL ACCOUNTS

  getAllAccounts(params: any): Observable<EntityAccountFieldResponseType> {
    return this.http.get<IAccountData[]>(`${this.url}account`, {
      params,
      observe: "response",
    });
  }

  // ADD ACCOUNT

  addAccount(accountDto: IAccountData): Observable<EntityAccountType> {
    return this.http.post(`${this.url}account`, accountDto, {
      observe: "response",
    });
  }

  // DELETE ACCOUNT

  deleteAccount(id?: any) {
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

  //   Update Account Status
  updateAccountStatus(id) {
    return this.http.put(`${this.url}account/status/${id}`, {});
  }
}
