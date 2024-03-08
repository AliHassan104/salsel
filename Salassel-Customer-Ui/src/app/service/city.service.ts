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

  //   GET SINGLE CITY
  getSingleCity(id: any) {
    return this.http.get(`${this.url}city/${id}`);
  }

  getAllCitiesByCountries(id: any) {
    return this.http.get(`${this.url}city/country/${id}`);
  }

  getAllCitiesByCountryName(name: any) {
    return this.http.get(`${this.url}city/country/name/${name}`);
  }

}
