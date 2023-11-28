import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { Ticket } from "src/app/api/ticket";

@Injectable({
  providedIn: "root",
})
export class AirbillService {
  constructor(private http: HttpClient) {}

  editBillMode = new BehaviorSubject(false);
  editBillId = new BehaviorSubject<any>("");
  createBillThrTickId = new BehaviorSubject<any>("");
  createBillThrTicMode = new BehaviorSubject(false);

  url = "http://localhost:8080/api/";

  // Create Ticket

  createBill(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}awb`, data);
  }

  //  Get All Tickets
  getBills() {
    return this.http.get(`${this.url}awb`);
  }

  //   Get Single Ticket

  getSingleBill(id) {
    return this.http.get(`${this.url}awb/${id}`);
  }

  //   Delete Ticket

  deleteBill(id) {
    return this.http.delete(`${this.url}awb/${id}`);
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
