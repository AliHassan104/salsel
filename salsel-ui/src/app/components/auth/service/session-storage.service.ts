import { Injectable } from "@angular/core";
import { AuthGuardService } from "./auth-guard.service";
import { BehaviorSubject } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class SessionStorageService {
  userPermissions: string[] = [];
  roleName: String;

  constructor(private authGuardSerivce: AuthGuardService) {}

  updatePermission() {
    const token = localStorage.getItem("token");
    const decodedToken = this.authGuardSerivce.getDecodedAccessToken(token!);

    if (decodedToken) {
      let decodedTokenPermissions = decodedToken.PERMISSIONS;
      this.userPermissions = decodedTokenPermissions;

      let role = decodedToken.ROLES;
      this.roleName = role[0];
    }
  }

  hasPermission(requiredPermissions: string | string[]): boolean {
    if (Array.isArray(requiredPermissions)) {
      // Check if the user has at least one of the required permissions in the array
      return requiredPermissions.some((permission) =>
        this.userPermissions.includes(permission)
      );
    } else {
      // Check if the user has the single required permission
      return this.userPermissions.includes(requiredPermissions);
    }
  }

  getRoleName() {
    return this.roleName;
  }
}
