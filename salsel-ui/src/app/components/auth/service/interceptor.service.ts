import {
  HTTP_INTERCEPTORS,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, finalize } from "rxjs";
import { LoaderService } from "../../loader/service/loader.service";
import { AuthService } from "./auth.service";

@Injectable({
  providedIn: "root",
})
export class LogininterceptorService implements HttpInterceptor {
  constructor(
    private LoaderService: LoaderService,
    private _authService: AuthService
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    this.LoaderService.showLoader();
    this._authService.getToken()
      ? (req = req.clone({
          headers: req.headers.set(
            "Authorization",
            `Bearer ${this._authService.getToken()}`
          ),
        }))
      : null;
    return next.handle(req).pipe(
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
