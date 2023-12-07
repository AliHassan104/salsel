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

  getAllCountries() {
    return this.http.get(`${this.url}country`);
  }
  // ADD COUNTRY

  addCountry(data: any): Observable<any> {
    return this.http.post(`${this.url}country`, data);
  }

  // DELETE COUNTRY

  deleteCountry(id: any) {
    return this.http.delete(`${this.url}country/${id}`);
  }

  // EDIT COUNTRY
  editCountry(id: any, data: any) {
    return this.http.put(`${this.url}country/${id}`, data);
  }

  //   GET SINGLE COUNTRY
  getSingleCountry(id: any) {
    return this.http.get(`${this.url}country/${id}`);
  }
}
