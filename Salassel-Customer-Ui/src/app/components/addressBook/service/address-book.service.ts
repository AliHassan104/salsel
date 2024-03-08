import { Injectable } from "@angular/core";
import { AddressBook, IAddressBook } from "../model/addressBookDto";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { Observable } from "rxjs";

export type EntityAddressBookType = HttpResponse<IAddressBook>;
export type EntityAddressBookResponseType = HttpResponse<IAddressBook[]>;

@Injectable({
  providedIn: "root",
})
export class AddressBookService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  create(addressBook: IAddressBook): Observable<EntityAddressBookType> {
    let url = `${this.url}address-book`;
    return this.http.post<IAddressBook>(url, addressBook, {
      observe: "response",
    });
  }

  update(data: any, id: any): Observable<any> {
    return this.http.put<any>(`${this.url}address-book/${id}`, data);
  }

  getAddressBooks(params: any): Observable<EntityAddressBookResponseType> {
    let url = `${this.url}address-book`;
    return this.http.get<AddressBook[]>(url, {
      params,
      observe: "response",
    });
  }

  getAddressBooksByUserType(userType: any): Observable<EntityAddressBookResponseType> {
    let url = `${this.url}address-book/user-type/${userType}`;
    return this.http.get<AddressBook[]>(url, {
      observe: "response",
    });
  }

  getAddressBookById(id?: any): Observable<EntityAddressBookType> {
    let url = `${this.url}address-book/${id}`;
    return this.http.get<AddressBook>(url, { observe: "response" });
  }

  removeAddressBook(id: any) {
    let url = `${this.url}address-book/${id}`;
    return this.http.delete<AddressBook>(url, { observe: "response" });
  }
}
