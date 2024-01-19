import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { IProductType, ProductType } from "../Model/productType.model";
import { environment } from "src/environments/environment";

export type EntityProductType = HttpResponse<IProductType>;
export type EntityProductTypeResponseType = HttpResponse<IProductType[]>;

@Injectable({
  providedIn: "root",
})
export class ServiceTypeService {
  url = environment.URL;

  constructor(private http: HttpClient) {}

  getServiceTypes(params: any): Observable<EntityProductTypeResponseType> {
    let url = `${this.url}service-type`;
    return this.http.get<ProductType[]>(url, { params, observe: "response" });
  }

  getServiceTypeById(id?: any): Observable<EntityProductType> {
    let url = `${this.url}service-type/${id}`;
    return this.http.get<ProductType>(url, { observe: "response" });
  }
}
