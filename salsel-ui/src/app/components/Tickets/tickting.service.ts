import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, Subject } from "rxjs";
import { Ticket } from "../../api/ticket";

@Injectable({
  providedIn: "root",
})
export class TicktingService {
  constructor(private http: HttpClient) {}

  editTicketMode = new BehaviorSubject(false);
  editId = new BehaviorSubject<any>("");

  url = "http://localhost:8080/api/";

  // Create Ticket

  createTicket(data: Ticket): Observable<any> {
    return this.http.post<any>(`${this.url}ticket`, data);
  }

  //  Get All Tickets
  getTickets() {
    return this.http.get(`${this.url}ticket`);
  }

  //   Get Single Ticket

  getSingleTicket(id) {
    return this.http.get(`${this.url}ticket/${id}`);
  }

  //   Delete Ticket

  deleteTicket(id) {
    return this.http.delete(`${this.url}ticket/${id}`);
  }
}
