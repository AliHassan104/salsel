import { HttpClient, HttpHeaders, HttpResponse } from "@angular/common/http";
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

  getAllAccountsByUserLoggedIn(
    params: any
  ): Observable<EntityAccountFieldResponseType> {
    return this.http.get<IAccountData[]>(`${this.url}account/logged-in-user`, {
      params,
      observe: "response",
    });
  }

  // ADD ACCOUNT

  addAccount(data: any, file: File) {
    const formData = new FormData();

    const accountDtoBlob = new Blob([JSON.stringify(data)], {
      type: "application/json",
    });
    formData.append("accountDto", accountDtoBlob, "accountDto.txt");

    formData.append("file", file);

    return this.http.post<any>(`${this.url}accountt`, formData);
  }

  // DELETE ACCOUNT

  deleteAccount(id?: any) {
    return this.http.delete(`${this.url}account/${id}`);
  }

  // EDIT ACCOUNT
  editAccount(id: any, data: any, file: File) {
    const formData = new FormData();

    const accountDtoBlob = new Blob([JSON.stringify(data)], {
      type: "application/json",
    });
    formData.append("accountDto", accountDtoBlob, "accountDto.txt");

    formData.append("file", file);

    return this.http.put<any>(`${this.url}account/${id}`, formData);
  }

  //   GET SINGLE ACCOUNT
  getSingleAccount(id: any) {
    return this.http.get(`${this.url}account/${id}`);
  }

  downloadAgreement(agreementUrl: any) {
    return this.http.get(`${this.url}file/${agreementUrl}`, {
      responseType: "blob" as "json", // Set the response type to 'blob'
    });
  }

  //   Update Account Status
  updateAccountStatus(id) {
    return this.http.put(`${this.url}account/status/${id}`, {});
  }

  downloadFile(data: any, filename: string) {
    const blob = new Blob([data], { type: "application/pdf" });
    const link = document.createElement("a");
    link.href = window.URL.createObjectURL(blob);
    link.download = filename;
    link.click();
  }
}
