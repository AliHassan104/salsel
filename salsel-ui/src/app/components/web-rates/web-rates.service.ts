import { Injectable } from '@angular/core';
import { IWebRate, WebRates } from '../web-rates/model/webRateDto';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

export type EntityRatesType = HttpResponse<IWebRate>;
export type EntityRatesResponseType = HttpResponse<IWebRate[]>;

@Injectable({
  providedIn: "root",
})
export class WebRatesService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  create(webRate: IWebRate): Observable<EntityRatesType> {
    let url = `${this.url}rates`;
    return this.http.post<WebRates>(url, webRate, {
      observe: "response",
    });
  }

  update(data: any, id: any): Observable<any> {
    return this.http.put<any>(`${this.url}rates/${id}`, data);
  }

  getAddressBooks(params: any): Observable<EntityRatesResponseType> {
    let url = `${this.url}rates`;
    return this.http.get<WebRates[]>(url, {
      params,
      observe: "response",
    });
  }

  getAddressBookById(id?: any): Observable<EntityRatesType> {
    let url = `${this.url}rates/${id}`;
    return this.http.get<WebRates>(url, { observe: "response" });
  }

  removeAddressBook(id: any) {
    let url = `${this.url}rates/${id}`;
    return this.http.delete<WebRates>(url, { observe: "response" });
  }

  updateAddressBookStatus(id: any) {
    let url = `${this.url}rates/status/${id}`;
    return this.http.put<WebRates>(url, { observe: "response" });
  }
}
