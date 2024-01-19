import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/internal/Observable";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class TicketCommentsService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  getAllTicketComments(params: any): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}ticket-comments`, { params });
  }

  createTicketComment(data: any): Observable<any> {
    return this.http.post<any>(`${this.url}ticket-comments`, data);
  }

  getTicketCommentById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}ticket-comments/${id}`);
  }

  deleteTicketComment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}ticket-comments/${id}`);
  }

  updateTicketComment(id: number, ticketComment: any): Observable<any> {
    return this.http.put<any>(
      `${this.url}ticket-comments/${id}`,
      ticketComment
    );
  }

  updateTicketCommentStatusToActive(id: number): Observable<void> {
    return this.http.put<void>(
      `${this.url}/ticket-comments/status/${id}`,
      null
    );
  }

  getAllTicketCommentsByTicketId(id: any): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}ticket-comments/ticket/${id}`);
  }
}
