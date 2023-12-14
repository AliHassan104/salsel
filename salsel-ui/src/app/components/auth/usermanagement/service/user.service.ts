import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class UserService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  getAllUser(params: any) {
    return this.http.get(`${this.url}user`, { params });
  }

  GetUserById(id: any) {
    return this.http.get(`${this.url}user/${id}`);
  }

  updateUser(id: any, data: any) {
    return this.http.put(`${this.url}user/${id}`, data);
  }

  deactivateUser(id: any) {
    return this.http.delete(`${this.url}user/${id}`);
  }

  getUserByName(name: string) {
    return this.http.get(`${this.url}user/name/${name}`);
  }

  updateUserStatus(id: any) {
    return this.http.put(`${this.url}user/status/${id}`, {});
  }
}
