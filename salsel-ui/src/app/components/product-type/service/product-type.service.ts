import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IProductFieldDto, ProductFieldDto } from 'src/app/api/productFieldDto';
import { IProductType, ProductType } from 'src/app/api/productType.model';
import { environment } from 'src/environments/environment';

export type EntityProductType = HttpResponse<IProductType>;
export type EntityProductTypeResponseType = HttpResponse<IProductType[]>;

@Injectable({
  providedIn: 'root'
})
export class ProductTypeService {

  url = environment.URL;

  constructor(private http: HttpClient) { }

    addProductType(productType: IProductType): Observable<EntityProductType> {
    let url = `${this.url}product-type`;
    return this.http
      .post<ProductType>(url, productType, { observe: 'response' })
    }

    getProductTypes(): Observable<EntityProductTypeResponseType> {
      let url = `${this.url}product-type`;
      return this.http
        .get<ProductType[]>(url, { observe: 'response' })
    }

    getProductTypeById(id?: any): Observable<EntityProductType> {
      let url = `${this.url}product-type/${id}`;
      return this.http
        .get<ProductType>(url, { observe: 'response' })
    }

    removeProductType(id: any){
      let url = `${this.url}product-type/${id}`;
      return this.http
        .delete<ProductType>(url, { observe: 'response' })
    }

}