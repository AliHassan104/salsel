import { Injectable } from "@angular/core";
import { Employee, IEmployee } from "../model/employeeDto";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { Observable } from "rxjs";

export type EntityEmployee = HttpResponse<IEmployee>;
export type EntityEmployeeResponseType = HttpResponse<IEmployee[]>;

@Injectable({
  providedIn: "root",
})
export class HrModuleService {
  constructor(private http: HttpClient) {}

  url = environment.URL;

  create(
    employee: Employee,
    passport: File,
    id: File,
    docs: File[]
  ): Observable<any> {
    const formData = new FormData();

    const employeeDtoBlob = new Blob([JSON.stringify(employee)], {
      type: "application/json",
    });
    formData.append("employeeDto", employeeDtoBlob, "employeeDto.txt");

    if (docs && docs.length > 0) {
      for (let i = 0; i < docs.length; i++) {
        formData.append(`docs`, docs[i]);
      }
    }
    if (passport) {
      formData.append("passport", passport);
    }

    if (id) {
      formData.append("id", id);
    }

    return this.http.post<any>(`${this.url}employee`, formData);
  }

  update(
    id: any,
    employee: Employee,
    params: any,
    passport: File,
    Nid: File,
    docs: File[]
  ) {
    const formData = new FormData();

    const employeeDtoBlob = new Blob([JSON.stringify(employee)], {
      type: "application/json",
    });
    formData.append("employeeDto", employeeDtoBlob, "employeeDto.txt");

    // const fileNames = new Blob([JSON.stringify(fileName)], {
    //   type: "application/json",
    // });
    // formData.append("fileNames", fileNames, "fileNames.txt");

    if (docs && docs.length > 0) {
      for (let i = 0; i < docs.length; i++) {
        formData.append(`files`, docs[i]);
      }
    }
    if (passport) {
      formData.append("passport", passport);
    }

    if (Nid) {
      formData.append("idFile", Nid);
    }

    return this.http.put<any>(`${this.url}employee/${id}`, formData, {
      params,
    });
  }

  getAllEmployees(params: any): Observable<EntityEmployeeResponseType> {
    return this.http.get<IEmployee[]>(`${this.url}employee`, {
      params,
      observe: "response",
    });
  }

  getEmployeeForm(id: any) {
    return this.http.get(`${this.url}employee/pdf/${id}`, {
      responseType: "blob" as "json",
    });
  }

  getEmployeeById(id?: any): Observable<EntityEmployee> {
    let url = `${this.url}employee/${id}`;
    return this.http.get<IEmployee>(url, { observe: "response" });
  }

  removeEmployee(id: any) {
    let url = `${this.url}employee/${id}`;
    return this.http.delete<IEmployee>(url, { observe: "response" });
  }

  updateEmployeeStatus(id: any) {
    let url = `${this.url}employee/status/${id}`;
    return this.http.put<any>(url, { observe: "response" });
  }

//   getBillingReports() {
//     return this.http.get(`${this.url}download-billing`, {
//       responseType: "blob" as "json",
//     });
//   }

//   getBillingReportsxl() {
//     return this.http.get(`${this.url}download-billing-xl`, {
//       responseType: "blob" as "json",
//     });
//   }

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
