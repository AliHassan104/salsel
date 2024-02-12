import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ITicketCategory, TicketCategory } from '../model/ticketCategoryDto';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

export type EntityTicketCategoryType = HttpResponse<ITicketCategory>;
export type EntityTicketCategoryResponseType = HttpResponse<ITicketCategory[]>;

@Injectable({
  providedIn: "root",
})
export class TicketCategoryService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  create(
    ticketCategory: ITicketCategory
  ): Observable<EntityTicketCategoryType> {
    let url = `${this.url}ticket-category`;
    return this.http.post<ITicketCategory>(url, ticketCategory, {
      observe: "response",
    });
  }

  update(data: any): Observable<any> {
    return this.http.put<any>(`${this.url}`, data);
  }

  getTicketCategories(
    params: any
  ): Observable<EntityTicketCategoryResponseType> {
    let url = `${this.url}ticket-category`;
    return this.http.get<TicketCategory[]>(url, {
      params,
      observe: "response",
    });
  }

  getTicketCategoryById(id?: any): Observable<EntityTicketCategoryType> {
    let url = `${this.url}ticket-category/${id}`;
    return this.http.get<TicketCategory>(url, { observe: "response" });
  }

  getTicketCategoryByDepartmentCaegory(
    id?: any
  ): Observable<EntityTicketCategoryResponseType> {
    let url = `${this.url}ticket-category/department-category/${id}`;
    return this.http.get<TicketCategory[]>(url, { observe: "response" });
  }

  removeTicketCategory(id: any) {
    let url = `${this.url}ticket-category/${id}`;
    return this.http.delete<TicketCategory>(url, { observe: "response" });
  }

  updateTicketCategoryStatus(id: any) {
    let url = `${this.url}ticket-category/status/${id}`;
    return this.http.put<TicketCategory>(url, { observe: "response" });
  }
}
