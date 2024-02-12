import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class UserService {
  constructor(private http: HttpClient) {}

  loginUserName: BehaviorSubject<any> = new BehaviorSubject(null);
  loginUserEmail: BehaviorSubject<any> = new BehaviorSubject(null);
  loginUser: BehaviorSubject<any> = new BehaviorSubject(null);

  url = environment.URL;

  getAllUser(params: any) {
    return this.http.get(`${this.url}user`, { params });
  }

  GetUserById(id: any) {
    return this.http.get(`${this.url}user/${id}`);
  }

  updateUser(id: any, data: any) {
    return this.http.patch(`${this.url}user/${id}`, data);
  }

  deactivateUser(id: any) {
    return this.http.delete(`${this.url}user/${id}`);
  }

  regeneratePassword(id: any) {
    return this.http.put(
      `${this.url}user/regenerate-password/${id}`,
      {},
      { responseType: "text" as "json" }
    );
  }

  changePassword(id: any, params: any) {
    return this.http.put(
      `${this.url}user/change-password/${id}`,
      {},
      { params, responseType: "text" as "json" }
    );
  }

  getUserByName(name: string) {
    return this.http.get(`${this.url}user/name/${name}`);
  }

  getUserByEmail(email: string) {
    return this.http.get(`${this.url}user/email/${email}`);
  }

  updateUserStatus(id: any) {
    return this.http.put(`${this.url}user/status/${id}`, {});
  }

  getMinMax() {
    return this.http.get(`${this.url}user/created-at-range`);
  }

  downloadUserDataInExcel(params: any) {
    return this.http.get(`${this.url}download-user-excel`, {
      params,
      responseType: "blob" as "json", // Set the response type to 'blob'
    });
  }

  downloadExcelFile(data: any, filename: string) {
    const blob = new Blob([data], { type: "application/xlsx" });
    const link = document.createElement("a");
    link.href = window.URL.createObjectURL(blob);
    link.download = filename;
    link.click();
  }
}
