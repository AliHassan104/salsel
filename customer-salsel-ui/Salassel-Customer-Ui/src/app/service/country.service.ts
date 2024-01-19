import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class CountryService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  //   GET ALL COUNTRIES

  getAllCountries(params: any) {
    return this.http.get(`${this.url}country`, { params });
  }

  //   GET SINGLE COUNTRY
  getSingleCountry(id: any) {
    return this.http.get(`${this.url}country/${id}`);
  }
}
