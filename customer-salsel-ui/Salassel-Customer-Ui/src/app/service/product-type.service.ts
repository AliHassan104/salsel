import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { environment } from "src/environments/environment";
import { IProductType, ProductType } from "../Model/productType.model";

export type EntityProductType = HttpResponse<IProductType>;
export type EntityProductTypeResponseType = HttpResponse<IProductType[]>;

@Injectable({
  providedIn: "root",
})
export class ProductTypeService {
  url = environment.URL;

  constructor(private http: HttpClient) {}

  getProductTypes(params: any): Observable<EntityProductTypeResponseType> {
    let url = `${this.url}product-type`;
    return this.http.get<ProductType[]>(url, { params, observe: "response" });
  }

  getProductTypeById(id?: any): Observable<EntityProductType> {
    let url = `${this.url}product-type/${id}`;
    return this.http.get<ProductType>(url, { observe: "response" });
  }
}
