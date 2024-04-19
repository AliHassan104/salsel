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
       !(
         userRoles.includes("ACCOUNT_HOLDER") ||
         userRoles.includes("CUSTOMER_USER")
       )
     ) {
       return true;
     } else {
       this.router.navigate(["/access"]); // Redirect to the unauthorized page
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
    const readEmployeeObj = {
      url: ["/employee/list", "/employee/list/:id"],
      permissions: "READ_EMPLOYEE",
    };
    const createEmployeeObj = {
      url: ["/create-employee"],
      permissions: "CREATE_EMPLOYEE",
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
    const readDepartmentObj = {
      url: ["/department/list"],
      permissions: "READ_DEPARTMENT",
    };
    const createDepartmentObj = {
      url: ["/create-department"],
      permissions: "CREATE_DEPARTMENT",
    };
    const readDepartmentCategoryObj = {
      url: ["/department-category/list"],
      permissions: "READ_DEPARTMENT_CATEGORY",
    };
    const createDepartmentCategoryObj = {
      url: ["/create-department-category"],
      permissions: "CREATE_DEPARTMENT_CATEGORY",
    };
    const readCityObj = {
      url: ["/city/list", "/city/list/:id"],
      permissions: "READ_CITY",
    };
    const createCityObj = {
      url: ["/create-city"],
      permissions: "CREATE_CITY",
    };
    const readCountryObj = {
      url: ["/country/list"],
      permissions: "READ_COUNTRY",
    };
    const createCountryObj = {
      url: ["/create-country"],
      permissions: "CREATE_COUNTRY",
    };
    const readProductTypeObj = {
      url: ["/product-type/list", "/product-type/list/:id"],
      permissions: "READ_PRODUCT_TYPE",
    };
    const createProductTypeObj = {
      url: ["/create-product-type"],
      permissions: "CREATE_PRODUCT_TYPE",
    };
    const readServiceTypeObj = {
      url: ["/service-type/list", "/service-type/list/:id"],
      permissions: "READ_SERVICE_TYPE",
    };
    const createServiceTypeObj = {
      url: ["/create-service-type"],
      permissions: "CREATE_SERVICE_TYPE",
    };
    const permissionsObj = {
      url: ["/permissions"],
      permissions: "PERMISSION",
    };
    const createUserObj = {
      url: ["/create-user"],
      permissions: "CREATE_USER",
    };
    const readUserObj = {
      url: ["/user/list", "/user/list/:id"],
      permissions: "READ_USER",
    };
    const accountHistoryrObj = {
      url: ["/awb-history/:id"],
      permissions: "READ_ACCOUNT",
    };
    const profileObj = {
      url: ["/profile"],
      permissions: "READ_USER",
    };
    const ticketCategoryObj = {
      url: ["/ticket-category/list"],
      permissions: "READ_TICKET_CATEGORY",
    };
    const createTicketCategoryObj = {
      url: ["/create-ticket-category"],
      permissions: "CREATE_TICKET_CATEGORY",
    };
    const ticketSubCategoryObj = {
      url: ["/ticket-sub-category/list"],
      permissions: "READ_TICKET_SUB_CATEGORY",
    };
    const createTicketSubCategoryObj = {
      url: ["/create-ticket-sub-category"],
      permissions: "CREATE_TICKET_SUB_CATEGORY",
    };
    const readAddressBookObj = {
      url: ["/address-book/list", "/address-book/list/:id"],
      permissions: "READ_ADDRESS_BOOK",
    };
    const createAddressBookObj = {
      url: ["/create-address-book"],
      permissions: "CREATE_ADDRESS_BOOK",
    };
    const trackingAndScanObj = {
      url: [
        "/tracking-and-scan",
        "/tracking-and-scan/scan",
        "/tracking-and-scan/tracking",
      ],
      permissions: "TRACKING_AND_SCAN",
    };

    return [
      dashboardObj,
      readAccountObj,
      createAccountObj,
      readAwbObj,
      createAwbObj,
      readCityObj,
      createCityObj,
      readCountryObj,
      createCountryObj,
      readDepartmentObj,
      createDepartmentObj,
      readDepartmentCategoryObj,
      createDepartmentCategoryObj,
      readTicketObj,
      createTicketObj,
      readProductTypeObj,
      createProductTypeObj,
      readServiceTypeObj,
      createServiceTypeObj,
      permissionsObj,
      createUserObj,
      readUserObj,
      accountHistoryrObj,
      profileObj,
      ticketCategoryObj,
      createTicketCategoryObj,
      ticketSubCategoryObj,
      createTicketSubCategoryObj,
      readAddressBookObj,
      createAddressBookObj,
      trackingAndScanObj,
      createEmployeeObj,
      readEmployeeObj
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
