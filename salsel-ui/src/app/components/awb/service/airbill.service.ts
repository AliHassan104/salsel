import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class AirbillService {
  constructor(private http: HttpClient) {}

  updateAWB = new BehaviorSubject(false);
  CreateAWB = new BehaviorSubject(false);

  url = environment.URL;

  // Create Ticket

  createBill(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}awb`, data);
  }

  //  Get All Tickets
  getBills(params: any) {
    return this.http.get(`${this.url}awb`, { params });
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
}
