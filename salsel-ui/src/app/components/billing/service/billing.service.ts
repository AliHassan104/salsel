import { Injectable } from '@angular/core';
import { IBilling } from '../model/billingDto';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';


export type EntityBilling = HttpResponse<IBilling>;
export type EntityBillingResponseType = HttpResponse<IBilling[]>;

@Injectable({
  providedIn: "root",
})
export class BillingService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  uploadExcelFileToGetData(file: File) {
    const formData = new FormData();

    formData.append("file", file);

    return this.http.post<any>(`${this.url}billing`, formData);
  }

  downloadUploadExcelFormat(){
    return this.http.get(`${this.url}file/Download/UploadInvoice.xlsx`, {
      observe: "response",
      responseType: "blob" as "json",
    });
  }
  downloadStatementExcelFormat(){
    return this.http.get(`${this.url}billing/salassil-statement`, {
      observe: "response",
      responseType: "blob" as "json",
    });
  }

  resendInvoice(id:any){
    return this.http.get(`${this.url}billing/resend-invoice/${id}`, {
      observe: "response",
    });
  }

  getAllInvoices(params: any): Observable<EntityBillingResponseType> {
    return this.http.get<IBilling[]>(`${this.url}billing`, {
      params,
      observe: "response",
    });
  }

  getInvoicePdf(id: any) {
    return this.http.get(`${this.url}billing/pdf/${id}`, {
      responseType: "blob" as "json",
    });
  }

  getInvoiceById(id?: any): Observable<EntityBilling> {
    let url = `${this.url}billing/${id}`;
    return this.http.get<IBilling>(url, { observe: "response" });
  }

  removeInvoice(id: any) {
    let url = `${this.url}billing/${id}`;
    return this.http.delete<IBilling>(url, { observe: "response" });
  }

  updateInvocieStatus(id: any) {
    let url = `${this.url}billing/status/${id}`;
    return this.http.put<any>(url, { observe: "response" });
  }

  getBillingReports() {
    return this.http.get(`${this.url}download-billing`, {
      responseType: "blob" as "json",
    });
  }

  getBillingReportsxl() {
    return this.http.get(`${this.url}download-billing-xl`, {
      responseType: "blob" as "json",
    });
  }

  downloadFile(data: any, filename: string) {
    const blob = new Blob([data], { type: "application/pdf" });
    const link = document.createElement("a");
    link.href = window.URL.createObjectURL(blob);
    link.download = filename;
    link.click();
  }

  downloadExcelFile(data: any, filename: string) {
    const blob = new Blob([data], { type: "application/xlsx" });
    const link = document.createElement("a");
    link.href = window.URL.createObjectURL(blob);
    link.download = filename;
    link.click();
  }
}
