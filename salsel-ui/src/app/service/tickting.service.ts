import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, Subject } from "rxjs";
import { Ticket } from "../api/ticket";

@Injectable({
  providedIn: "root",
})
export class TicktingService {
  constructor(private http: HttpClient) {}

  editTicketMode = new BehaviorSubject(false);
  editId = new BehaviorSubject<any>("");

  url = "http://localhost:5000/api/v1/";

  // Create Ticket

  createTicket(data: Ticket): Observable<any> {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.post<any>(`${this.url}notes/new`, data, { headers });
  }

  //  Get All Tickets
  getTickets() {
    return this.http.get(`http://localhost:8080/api/ticket`);
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
