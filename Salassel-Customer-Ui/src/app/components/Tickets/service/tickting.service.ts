import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, Subject } from "rxjs";
import { Ticket } from "../model/ticketValuesDto";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class TicktingService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  // Create Ticket

  createTicket(data: Ticket, files: File[]): Observable<any> {
    const formData = new FormData();

    const ticketDtoBlob = new Blob([JSON.stringify(data)], {
      type: "application/json",
    });
    formData.append("ticketDto", ticketDtoBlob, "ticketDto.txt");

    if (files && files.length > 0) {
      for (let i = 0; i < files.length; i++) {
        formData.append(`files`, files[i]);
      }
    }

    return this.http.post<any>(`${this.url}ticket`, formData);
  }

  getMinMax() {
    return this.http.get(`${this.url}ticket/created-at-range`);
  }

  downloadTicketDataInExcel(params: any) {
    return this.http.get(`${this.url}download-ticket-excel`, {
      params,
      responseType: "blob" as "json", // Set the response type to 'blob'
    });
  }

  //  Get All Tickets
  getTickets(params?: any) {
    return this.http.get(`${this.url}ticket`, { params, observe: "response" });
  }

  getTicketsByLoggedInUserAndRole(params?: any) {
    return this.http.get(`${this.url}ticket/logged-in-user-and-role`, {
      params,
      observe: "response",
    });
  }

  getLoggedInUserTickets(params?: any) {
    return this.http.get(`${this.url}ticket/logged-in-user`, {
      params,
      observe: "response",
    });
  }

  //   Get Single Ticket

  getSingleTicket(id) {
    return this.http.get(`${this.url}ticket/${id}`);
  }

  //   Delete Ticket

  deleteTicket(id) {
    return this.http.delete(`${this.url}ticket/${id}`);
  }

  //   Update Ticket Status
  updateTicketStatus(id) {
    return this.http.put(`${this.url}ticket/status/${id}`, {});
  }

  editTicket(id: any, data: any, files: File[], params: any) {
    const formData = new FormData();

    const ticketDtoBlob = new Blob([JSON.stringify(data)], {
      type: "application/json",
    });
    formData.append("ticketDto", ticketDtoBlob, "ticketDto.txt");

    if (files && files.length > 0) {
      for (let i = 0; i < files.length; i++) {
        formData.append(`files`, files[i]);
      }
    }

    return this.http.put<any>(`${this.url}ticket/${id}/filenames`, formData, {
      params,
    });
  }

  getStatusCount() {
    return this.http.get(`${this.url}ticket/status-counts`);
  }

  getStatusCountByLoggedInUser() {
    return this.http.get(`${this.url}ticket/logged-in-user-status-counts`);
  }

  //   Get formated Date
  formatDate(date: Date): string {
    if (date != null) {
      const year = date.getFullYear().toString(); // Get the last two digits of the year
      const month = (date.getMonth() + 1).toString().padStart(2, "0");
      const day = date.getDate().toString().padStart(2, "0");
      return `${year}-${month}-${day}`;
    }
    return null;
  }

  formatCreatedAt(date: Date): string {
    if (date != null) {
      const year = date.getFullYear().toString();
      const month = (date.getMonth() + 1).toString().padStart(2, "0");
      const day = date.getDate().toString().padStart(2, "0");
      const hours = date.getHours().toString().padStart(2, "0");
      const minutes = date.getMinutes().toString().padStart(2, "0");
      const seconds = date.getSeconds().toString().padStart(2, "0");

      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    } else {
      return null;
    }
  }

  formatDateToHHMMSS(date: Date): string {
    if (date != null) {
      const hours = date.getHours().toString().padStart(2, "0");
      const minutes = date.getMinutes().toString().padStart(2, "0");
      const seconds = date.getSeconds().toString().padStart(2, "0");

      return `${hours}:${minutes}:${seconds}`;
    } else {
      return null;
    }
  }

  downloadExcelFile(data: any, filename: string) {
    const blob = new Blob([data], { type: "application/xlsx" });
    const link = document.createElement("a");
    link.href = window.URL.createObjectURL(blob);
    link.download = filename;
    link.click();
  }
}
