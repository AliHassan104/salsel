import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import {
  IProductFieldDto,
  ProductFieldDto,
} from "src/app/components/product-field/model/productFieldDto";
import {
  IProductType,
  ProductType,
} from "src/app/components/product-type/model/productType.model";
import { environment } from "src/environments/environment";

export type EntityProductType = HttpResponse<IProductType>;
export type EntityProductTypeResponseType = HttpResponse<IProductType[]>;

@Injectable({
  providedIn: "root",
})
export class ServiceTypeService {
  url = environment.URL;

  constructor(private http: HttpClient) {}

  addServiceType(productType: IProductType): Observable<EntityProductType> {
    let url = `${this.url}service-type`;
    return this.http.post<ProductType>(url, productType, {
      observe: "response",
    });
  }

  getServiceTypes(params: any): Observable<EntityProductTypeResponseType> {
    let url = `${this.url}service-type`;
    return this.http.get<ProductType[]>(url, { params, observe: "response" });
  }

  getServiceTypeById(id?: any): Observable<EntityProductType> {
    let url = `${this.url}service-type/${id}`;
    return this.http.get<ProductType>(url, { observe: "response" });
  }

  removeServiceType(id: any) {
    let url = `${this.url}service-type/${id}`;
    return this.http.delete<ProductType>(url, { observe: "response" });
  }

  updateServiceTypeStatus(id: any) {
    let url = `${this.url}service-type/status/${id}`;
    return this.http.put<ProductType>(url, { observe: "response" });
  }
}
