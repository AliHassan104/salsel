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

      if (userPermissions.includes(permission.permissions)) {
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
      permissions: "Dashboard",
    };
    const ticketObj = {
      url: ["/ticket", "/ticket/list", "/ticket/list/:id"],
      permissions: "Ticket",
    };
    const awbObj = {
      url: ["/awb", "/awb/list", "/awb/list/:id"],
      permissions: "Awb",
    };
    const accountObj = {
      url: ["/account", "/account/list", "/account/list/:id"],
      permissions: "Account",
    };
    const departmentObj = {
      url: ["/department", "/department/list"],
      permissions: "Department",
    };
    const departmentCategoryObj = {
      url: ["/department-category", "/department-category/list"],
      permissions: "Department Category",
    };
    const cityObj = {
      url: ["/city", "/city/list"],
      permissions: "City",
    };
    const countryObj = {
      url: ["/country", "/country/list"],
      permissions: "Country",
    };
    const productTypeObj = {
      url: ["/product-type", "/product-type/list"],
      permissions: "Product Type",
    };
    const ServiceTypeObj = {
      url: ["/service-type", "/service-type/list"],
      permissions: "Product Type",
    };
    const permissionsObj = {
      url: ["/permissions"],
      permissions: "Permission",
    };
    const userObj = {
      url: ["/user", "/user/list", "/user/list/:id"],
      permissions: "User",
    };

    return [
      dashboardObj,
      ticketObj,
      awbObj,
      accountObj,
      cityObj,
      countryObj,
      productTypeObj,
      ServiceTypeObj,
      departmentCategoryObj,
      departmentObj,
      permissionsObj,
      userObj,
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
