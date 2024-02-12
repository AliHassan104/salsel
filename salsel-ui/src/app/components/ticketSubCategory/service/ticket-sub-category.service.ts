import { Injectable } from '@angular/core';
import { ITicketSubCategory, TicketSubCategory } from '../model/ticketSubCategoryDeto';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

export type EntityTicketSubCategoryType = HttpResponse<ITicketSubCategory>;
export type EntityTicketSubCategoryResponseType = HttpResponse<ITicketSubCategory[]>;

@Injectable({
  providedIn: "root",
})
export class TicketSubCategoryService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  create(
    TicketSubCategory: ITicketSubCategory
  ): Observable<EntityTicketSubCategoryType> {
    let url = `${this.url}ticket-sub-category`;
    return this.http.post<ITicketSubCategory>(url, TicketSubCategory, {
      observe: "response",
    });
  }

  update(data: any): Observable<any> {
    return this.http.put<any>(`${this.url}`, data);
  }

  getTicketSubCategories(
    params: any
  ): Observable<EntityTicketSubCategoryResponseType> {
    let url = `${this.url}ticket-sub-category`;
    return this.http.get<TicketSubCategory[]>(url, {
      params,
      observe: "response",
    });
  }

  getTicketSubCategoryById(id?: any): Observable<EntityTicketSubCategoryType> {
    let url = `${this.url}ticket-sub-category/${id}`;
    return this.http.get<TicketSubCategory>(url, { observe: "response" });
  }

  getTicketSubCategoryByTicketCategory(
    id?: any
  ): Observable<EntityTicketSubCategoryResponseType> {
    let url = `${this.url}ticket-sub-category/ticket-category/${id}`;
    return this.http.get<TicketSubCategory[]>(url, { observe: "response" });
  }

  removeTicketSubCategory(id: any) {
    let url = `${this.url}ticket-sub-category/${id}`;
    return this.http.delete<TicketSubCategory>(url, { observe: "response" });
  }

  updateTicketSubCategoryStatus(id: any) {
    let url = `${this.url}ticket-sub-category/status/${id}`;
    return this.http.put<TicketSubCategory>(url, { observe: "response" });
  }
}
