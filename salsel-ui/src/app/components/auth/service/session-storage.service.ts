import { Injectable } from "@angular/core";
import { AuthGuardService } from "./auth-guard.service";

@Injectable({
  providedIn: "root",
})
export class SessionStorageService {
  userPermissions: string[] = [];

  constructor(private authGuardSerivce: AuthGuardService) {
    this.updatePermission();
  }

  updatePermission() {
    const token = localStorage.getItem("token");
    const decodedToken = this.authGuardSerivce.getDecodedAccessToken(token!);

    if (decodedToken) {
      let decodedTokenPermissions = decodedToken.PERMISSIONS;
      this.userPermissions = decodedTokenPermissions;
    }
  }

  hasPermission(requiredPermission: string): boolean {
    return this.userPermissions.includes(requiredPermission);
  }
}
