import { Injectable, inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../service/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
  const _authService = inject(AuthService);
  const router = inject(Router);
  let token = _authService.idLoggedIn();
  if (token) {
    return true;
  } else {
    router.navigate(["login"]);
    return false;
  }
};
