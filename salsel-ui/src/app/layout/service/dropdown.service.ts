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

  //   GET ALL COUNTRIES
  getAllCountries() {
    return this.http.get(`${this.url}country`);
  }

  //   GET ALL CITIES
  getAllCities() {
    return this.http.get(`${this.url}city`);
  }

  //   GET ALL DEPARTMENTS
  getAllDepartments() {
    return this.http.get(`${this.url}department`);
  }

  //   GET ALL DEPARTMENTS
  getAllDepartmentCategories() {
    return this.http.get(`${this.url}department-category`);
  }

  //   GET ALL PRODUCT TYPES
  getAllProductTypes() {
    return this.http.get(`${this.url}product-type`);
  }

  //   GET ALL SERVICE TYPES
  getAllServiceTypes() {
    return this.http.get(`${this.url}service-type`);
  }

  // Function to extract 'name' attributes from an array of objects
  extractNames(array: any[]): string[] {
    return array.map<any>((item) => item.name);
  }
  extractCode(array: any[]): string[] {
    return array.map<any>((item) => item.code);
  }
  extractaccountNumber(array: any[]): string[] {
    return array.map<any>((item) => item.accountNumber);
  }
}
