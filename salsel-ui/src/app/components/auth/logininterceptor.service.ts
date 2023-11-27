import { HttpEvent, HttpHandler, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class LogininterceptorService {
  constructor() {}

  token = localStorage.getItem("jwt")
    ? JSON.parse(localStorage.getItem("jwt"))
    : "";

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let tokenheadfer = req.clone({
      setHeaders: {
        Authorization: `Bearer ${this.token}`,
      },
    });
    return next.handle(tokenheadfer);
  }
}
