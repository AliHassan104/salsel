import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { TicketData } from "../api/ticketdata";
import { BehaviorSubject, Observable, Subject } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class TicktingService {
  constructor(private http: HttpClient) {}

  editTicketMode = new BehaviorSubject(true);
  editId = new BehaviorSubject<any>("");

  url = "http://localhost:5000/api/v1/";

  // Create Ticket

  createTicket(data: TicketData): Observable<any> {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.post<any>(`${this.url}notes/new`, data, { headers });
  }

  //  Get All Tickets
  getTickets() {
    return this.http.get(`${this.url}notes`);
  }

  //   Get Single Ticket

  getSingleTicket(id) {
    return this.http.get(`${this.url}notes/${id}`);
  }

  //   Delete Ticket

  deleteTicket(id) {
    return this.http.delete(`${this.url}notes/${id}`);
  }
}
