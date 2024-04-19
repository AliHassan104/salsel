import { Injectable } from "@angular/core";
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from "@angular/router";
import { Observable } from "rxjs";
import { jwtDecode } from "jwt-decode";

@Injectable({
  providedIn: "root",
})
export class AuthGuardService implements CanActivate {
  constructor(private router: Router) {}

  get token(): string {
    return localStorage.getItem("token")!;
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const jwtToken = localStorage.getItem("token");
    if (jwtToken) {
      const decodedToken = this.getDecodedAccessToken(jwtToken);
      const userPermissions = decodedToken.PERMISSIONS;
      const userRoles = decodedToken.ROLES;


      const url = state.url;
      let permission: any = {};
      const permissionsObj = this.getPermissionsObj();
      const matchingPermission = permissionsObj.find((p: any) =>
        p.url.some((u: any) => this.urlMatches(u, url))
      );

      if (matchingPermission) {
        permission = matchingPermission;
      }

      if (
        userPermissions.includes(permission.permissions) &&
        (userRoles[0] == "ACCOUNT_HOLDER" || userRoles[0] == "ADMIN")
      ) {
        return true;
      } else {
        this.router.navigate(["/access"]); // Redirect to an unauthorized page or handle it as needed
        return false;
      }
    } else {
      this.router.navigate(["/login"]);
      return false;
    }
  }

  getDecodedAccessToken(token: string): any {
    try {
      return jwtDecode(token);
    } catch (Error) {
      console.error("Error decoding JWT token:" + Error);
    }
  }

  getPermissionsObj(): any {
    const dashboardObj = {
      url: ["/"],
      permissions: "DASHBOARD",
    };
    const readTicketObj = {
      url: ["/ticket/list", "/ticket/list/:id"],
      permissions: "READ_TICKET",
    };
    const createTicketObj = {
      url: ["/create-ticket"],
      permissions: "CREATE_TICKET",
    };
    const readBookingObj = {
      url: ["/booking/list", "/booking/list/:id"],
      permissions: "READ_TICKET",
    };
    const createBookingObj = {
      url: ["/booking-ticket"],
      permissions: "CREATE_TICKET",
    };
    const readAwbObj = {
      url: ["/awb/list", "/awb/list/:billid"],
      permissions: "READ_AWB",
    };
    const createAwbObj = {
      url: ["/create-awb"],
      permissions: "CREATE_AWB",
    };
    const readAccountObj = {
      url: ["/account/list", "/account/list/:id"],
      permissions: "READ_ACCOUNT",
    };
    const createAccountObj = {
      url: ["/create-account"],
      permissions: "CREATE_ACCOUNT",
    };
    const permissionsObj = {
      url: ["/permissions"],
      permissions: "PERMISSION",
    };
    const accountHistoryrObj = {
      url: ["/awb-history/:id"],
      permissions: "READ_ACCOUNT",
    };
    const profileObj = {
      url: ["/profile"],
      permissions: "READ_USER",
    };
    const readAddressBookObj = {
      url: ["/address-book/list", "/address-book/list/:id"],
      permissions: "READ_ADDRESS_BOOK",
    };
    const createAddressBookObj = {
      url: ["/create-address-book"],
      permissions: "CREATE_ADDRESS_BOOK",
    };

    return [
      dashboardObj,
      readAccountObj,
      createAccountObj,
      readAwbObj,
      createAwbObj,
      readTicketObj,
      createTicketObj,
      permissionsObj,
      accountHistoryrObj,
      profileObj,
      readAddressBookObj,
      createAddressBookObj,
      readBookingObj,
      createBookingObj
    ];
  }
  private urlMatches(pattern: string, url: string): boolean {
    const patternSegments = pattern.split("?")[0].split("/"); // Get URL segments without query parameters
    const urlSegments = url.split("?")[0].split("/"); // Get URL segments without query parameters

    if (patternSegments.length !== urlSegments.length) {
      return false; // URLs have different segment counts
    }

    for (let i = 0; i < patternSegments.length; i++) {
      if (
        patternSegments[i] !== urlSegments[i] &&
        !patternSegments[i].startsWith(":")
      ) {
        return false; // Segments don't match and are not parameters
      }
    }

    return true; // All segments match or are parameters
  }
}
