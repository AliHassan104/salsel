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

  createBill(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}awb`, data);
  }

  getBills(params: any) {
    return this.http.get(`${this.url}awb`, { params });
  }

  getBillsByUserRole(params: any) {
    return this.http.get(`${this.url}awb/logged-in-user-role`, { params });
  }

  getBillsByUserEmailAndRole(params: any) {
    return this.http.get(`${this.url}awb/logged-in-user-and-role`, { params });
  }

  getSingleBill(id: any) {
    return this.http.get(`${this.url}awb/${id}`);
  }

  deleteBill(id: any) {
    return this.http.delete(`${this.url}awb/${id}`);
  }

  updateAssignedTo(id: any, data: any) {
    return this.http.put(`${this.url}awb/${id}`, data);
  }

  updateBillStatus(id) {
    return this.http.put(`${this.url}awb/status/${id}`, {});
  }

  downloadBill(id: any) {
    return this.http.get(`${this.url}awb/pdf/awb_${id}/${id}`, {
      responseType: "blob" as "json",
    });
  }

  getStatusCount() {
    return this.http.get(`${this.url}awb/status-counts`);
  }

  getAwbStatusCount() {
    return this.http.get(`${this.url}awb/awb-status-counts`);
  }

  getAwbStatusByLoggedInUser() {
    return this.http.get(`${this.url}awb/logged-in-user-awb-status-counts`);
  }

  getStatusCountByLoggedInUser() {
    return this.http.get(`${this.url}awb/logged-in-user-status-counts`);
  }

  updateAwbTrackingStatus(status: any, uniqueNumber: any) {
    return this.http.put(
      `${this.url}awb/awb-status/${status}/unique-number/${uniqueNumber}`,
      {}
    );
  }

  getBillTrackingHistory(params: any) {
    return this.http.get(`${this.url}awb-shipping-history`, { params });
  }

  downloadAwbDataInExcel(params: any) {
    return this.http.get(`${this.url}download-awb-excel`, {
      params,
      responseType: "blob" as "json", // Set the response type to 'blob'
    });
  }

  getMinMax() {
    return this.http.get(`${this.url}awb/created-at-range`);
  }

  downloadExcelFile(data: any, filename: string) {
    const blob = new Blob([data], { type: "application/xlsx" });
    const link = document.createElement("a");
    link.href = window.URL.createObjectURL(blob);
    link.download = filename;
    link.click();
  }

  formatDate(date: Date): string {
    const year = date.getFullYear().toString();
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
