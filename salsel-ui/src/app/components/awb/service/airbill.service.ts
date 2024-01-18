import { HttpClient, HttpHeaders, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { AuthGuardService } from "../../auth/service/auth-guard.service";

@Injectable({
  providedIn: "root",
})
export class AirbillService {
  constructor(
    private http: HttpClient,
    private _authService: AuthGuardService
  ) {}

  updateAWB = new BehaviorSubject(false);
  CreateAWB = new BehaviorSubject(false);
  jwtToken = localStorage.getItem("token");
  url = environment.URL;

  // Create Ticket

  createBill(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}awb`, data);
  }

  //  Get All Tickets
  getBills(params: any) {
    return this.http.get(`${this.url}awb`, { params });
  }

  getBillsByUserRole(params: any) {
    return this.http.get(`${this.url}awb/logged-in-user-role`, { params });
  }

  getBillsByUserEmailAndRole(params: any) {
    return this.http.get(`${this.url}awb/logged-in-user-and-role`, { params });
  }

  //   Get Single Ticket

  getSingleBill(id) {
    return this.http.get(`${this.url}awb/${id}`);
  }

  //   Delete Ticket

  deleteBill(id) {
    return this.http.delete(`${this.url}awb/${id}`);
  }

  //   Update Bill Sttaus
  updateBillStatus(id) {
    return this.http.put(`${this.url}awb/status/${id}`, {});
  }

  downloadBill(id: any) {
    return this.http.get(`${this.url}awb/pdf/awb_${id}/${id}`, {
      responseType: "blob" as "json", // Set the response type to 'blob'
    });
  }

  //   Get formated Date
  formatDate(date: Date): string {
    const year = date.getFullYear().toString(); // Get the last two digits of the year
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  formatCreatedAt(date: Date): string {
    const year = date.getFullYear().toString();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    const hours = date.getHours().toString().padStart(2, "0");
    const minutes = date.getMinutes().toString().padStart(2, "0");
    const seconds = date.getSeconds().toString().padStart(2, "0");

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }

  formatDateToHHMMSS(date: Date): string {
    const hours = date.getHours().toString().padStart(2, "0");
    const minutes = date.getMinutes().toString().padStart(2, "0");
    const seconds = date.getSeconds().toString().padStart(2, "0");

    return `${hours}:${minutes}:${seconds}`;
  }

  downloadFile(data: any, filename: string) {
    const blob = new Blob([data], { type: "application/pdf" });
    const link = document.createElement("a");
    link.href = window.URL.createObjectURL(blob);
    link.download = filename;
    link.click();
  }
}
