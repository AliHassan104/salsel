import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class DropdownService {
  url = environment.URL;

  constructor(private http: HttpClient) {}

  //   Get All Product Fields
  getAllProductFields() {
    return this.http.get(`${this.url}product-field`);
  }

  //   Get Product Field by ID
  getProductFieldById(id: string) {
    return this.http.get(`${this.url}product-field/` + id);
  }

  //Get Product Field by Product Field Name
  getProductFieldByName(name: string) {
    return this.http.get(`${this.url}product-field/name/${name}`);
  }
}
