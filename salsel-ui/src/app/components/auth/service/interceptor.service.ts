import {
  HTTP_INTERCEPTORS,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, finalize, throwError } from "rxjs";
import { LoaderService } from "../../loader/service/loader.service";
import { AuthService } from "./auth.service";
import { AuthGuardService } from "./auth-guard.service";
import { Router } from "@angular/router";

@Injectable({
  providedIn: "root",
})
export class LogininterceptorService implements HttpInterceptor {
  constructor(
    private LoaderService: LoaderService,
    private _authService: AuthGuardService,
    private router: Router
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    this.LoaderService.showLoader();

    const token = this._authService.token;

    if (token) {
      const expiryTime = this._authService.getDecodedAccessToken(token).exp;
      const currentTimestamp = Math.floor(Date.now() / 1000);

      if (expiryTime && expiryTime <= currentTimestamp) {
        // Token has expired, navigate to login page
        this.router.navigate(["/login"]);
        this.LoaderService.hideLoader();
        return throwError("Token has expired"); // Stop further processing
      }

      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(req).pipe(
      catchError((error) => {
        // Handle errors if needed
        return throwError(error);
      }),
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
