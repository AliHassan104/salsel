import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { an } from "@fullcalendar/core/internal-common";
import { Observable } from "rxjs";
import {
  IProductFieldDto,
  ProductFieldDto,
} from "src/app/components/product-field/model/productFieldDto";
import { environment } from "src/environments/environment";

export type EntityProductFieldType = HttpResponse<IProductFieldDto>;
export type EntityProductFieldResponseType = HttpResponse<IProductFieldDto[]>;

@Injectable({
  providedIn: "root",
})
export class ProductFieldService {
  url = environment.URL;

  constructor(private http: HttpClient) {}

  addProductFields(
    productFieldDto: IProductFieldDto
  ): Observable<EntityProductFieldType> {
    let url = `${this.url}product-field`;
    return this.http.post<ProductFieldDto>(url, productFieldDto, {
      observe: "response",
    });
  }

  getProductFields(): Observable<EntityProductFieldResponseType> {
    let url = `${this.url}product-field`;
    return this.http.get<ProductFieldDto[]>(url, { observe: "response" });
  }

  getProductFieldById(id?: any): Observable<EntityProductFieldType> {
    let url = `${this.url}product-field/${id}`;
    return this.http.get<ProductFieldDto>(url, { observe: "response" });
  }

  removeProductFieldValue(id: any, pfvId?: any) {
    let url = `${this.url}product-field/${id}/${pfvId}`;
    return this.http.delete<ProductFieldDto>(url, { observe: "response" });
  }

  removeProductField(id: any) {
    let url = `${this.url}product-field/${id}`;
    return this.http.delete<ProductFieldDto>(url, { observe: "response" });
  }
}
