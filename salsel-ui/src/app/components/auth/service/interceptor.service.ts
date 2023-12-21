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
import { AuthGuardService } from "./auth-guard.service";

@Injectable({
  providedIn: "root",
})
export class LogininterceptorService implements HttpInterceptor {
  constructor(
    private LoaderService: LoaderService,
    private _authService: AuthGuardService
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    this.LoaderService.showLoader();
    this._authService.token
      ? (req = req.clone({
          headers: req.headers.set(
            "authorization",
            `Bearer ${this._authService.token}`
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
