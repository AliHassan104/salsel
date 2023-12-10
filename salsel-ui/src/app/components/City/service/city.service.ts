import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class CityService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  //   GET ALL COUNTRIES

  getAllCities(params: any) {
    return this.http.get(`${this.url}city`, { params });
  }
  // ADD CITY

  addCity(data: any): Observable<any> {
    return this.http.post(`${this.url}city`, data);
  }

  // DELETE CITY

  deleteCity(id: any) {
    return this.http.delete(`${this.url}city/${id}`);
  }

  // EDIT CITY
  editCity(id: any, data: any) {
    return this.http.put(`${this.url}city/${id}`, data);
  }

  //   GET SINGLE CITY
  getSingleCity(id: any) {
    return this.http.get(`${this.url}city/${id}`);
  }

  //   Update City Status
  updateCityStatus(id: any) {
    return this.http.put(`${this.url}city/status/${id}`, {});
  }
}
