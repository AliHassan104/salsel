import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class TrackingService {
  url = environment.URL;
  constructor(private http: HttpClient) {}

  downloadTrackingDataInExcel(awbNumbers: number[]) {
    let params = new HttpParams();

    awbNumbers.forEach((num) => {
      params = params.append("trackingNumbers", num.toString());
    });

    return this.http.get(`${this.url}download-tracking-excel`, {
      params,
      responseType: "blob" as "json", // Set the response type to 'blob'
    });
  }
}
