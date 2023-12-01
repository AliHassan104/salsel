import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, Subject } from "rxjs";
import { Ticket } from "../../../api/ticket";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class TicktingService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  // Create Ticket

  createTicket(data: Ticket): Observable<any> {
    return this.http.post<any>(`${this.url}ticket`, data);
  }

  //  Get All Tickets
  getTickets(params?: any) {
    return this.http.get(`${this.url}ticket`,  { params, observe: 'response' });
  }

  //   Get Single Ticket

  getSingleTicket(id) {
    return this.http.get(`${this.url}ticket/${id}`);
  }

  //   Delete Ticket

  deleteTicket(id) {
    return this.http.delete(`${this.url}ticket/${id}`);
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
