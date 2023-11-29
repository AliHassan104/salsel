import {
  HTTP_INTERCEPTORS,
  HttpEvent,
  HttpHandler,
  HttpRequest,
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, finalize } from "rxjs";
import { LoaderService } from "../../../service/loader.service";

@Injectable({
  providedIn: "root",
})
export class LogininterceptorService {
  constructor(private LoaderService: LoaderService) {}

  token = localStorage.getItem("jwt")
    ? JSON.parse(localStorage.getItem("jwt"))
    : "";

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    this.LoaderService.showLoader();
    let tokenheadfer = req.clone({
      setHeaders: {
        Authorization: `Bearer ${this.token}`,
      },
    });
    return next.handle(tokenheadfer).pipe(
      finalize(() => {
        this.LoaderService.hideLoader();
      })
    );
  }
}

export const AuthInterceptorProvider = {
  provide: HTTP_INTERCEPTORS,
  useClass: LogininterceptorService,
  multi: true,
};
