import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { an } from "@fullcalendar/core/internal-common";
import { Observable } from "rxjs";
import {
  IProductFieldDto,
  ProductFieldDto,
} from "src/app/Model/productFieldDto";
import { environment } from "src/environments/environment";

export type EntityProductFieldType = HttpResponse<IProductFieldDto>;
export type EntityProductFieldResponseType = HttpResponse<IProductFieldDto[]>;

@Injectable({
  providedIn: "root",
})
export class ProductFieldService {
  url = environment.URL;

  constructor(private http: HttpClient) {}

  getProductFields(): Observable<EntityProductFieldResponseType> {
    let url = `${this.url}product-field`;
    return this.http.get<ProductFieldDto[]>(url, { observe: "response" });
  }

  getProductFieldById(id?: any): Observable<EntityProductFieldType> {
    let url = `${this.url}product-field/${id}`;
    return this.http.get<ProductFieldDto>(url, { observe: "response" });
  }
}
