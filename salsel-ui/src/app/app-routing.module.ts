import { RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { NotfoundComponent } from "./components/notfound/notfound.component";
import { AppLayoutComponent } from "./layout/app.layout.component";
import { LoginComponent } from "./components/auth/login/login.component";
import { AccessdeniedComponent } from "./components/auth/accessdenied/accessdenied.component";
import { PermissionsComponent } from "./components/permissions/permissions.component";
import { AuthGuardService } from "./components/auth/service/auth-guard.service";
import { ForgotPasswordComponent } from "./components/auth/forgot-password/forgot-password.component";
import { NewPasswordComponent } from "./components/auth/new-password/new-password.component";
import { UserProfileComponent } from "./components/auth/usermanagement/user-profile/user-profile.component";
import { VerificationComponent } from "./components/auth/verification/verification.component";

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: "",
          component: AppLayoutComponent,
          canActivate: [AuthGuardService],
          children: [
            {
              path: "",
              loadChildren: () =>
                import("./components/dashboard/dashboard.module").then(
                  (m) => m.DashboardModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "user/list",
              loadChildren: () =>
                import(
                  "./components/auth/usermanagement/usermanagement.module"
                ).then((m) => m.UsermanagementModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-user",
              loadChildren: () =>
                import(
                  "./components/auth/usermanagement/update/user-form.module"
                ).then((m) => m.UserFormModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "ticket/list",
              loadChildren: () =>
                import("./components/Tickets/tickets.module").then(
                  (m) => m.TicketsModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-ticket",
              loadChildren: () =>
                import("./components/Tickets/update/tickets-form.module").then(
                  (m) => m.TicketsFormModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "awb/list",
              loadChildren: () =>
                import("./components/awb/airbill.module").then(
                  (m) => m.AirbillModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-awb",
              loadChildren: () =>
                import("./components/awb/update/awbform.module").then(
                  (m) => m.AwbformModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "department/list",
              loadChildren: () =>
                import("./components/department/department.module").then(
                  (m) => m.DepartmentModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-department",
              loadChildren: () =>
                import(
                  "./components/department/update/department-form.module"
                ).then((m) => m.DepartmentFormModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "department-category/list",
              loadChildren: () =>
                import(
                  "./components/department-category/department-category.module"
                ).then((m) => m.DepartmentCategoryModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-department-category",
              loadChildren: () =>
                import(
                  "./components/department-category/update/department-category-form.module"
                ).then((m) => m.DepartmentCategoryFormModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "product",
              loadChildren: () =>
                import("./components/product-field/product-field.module").then(
                  (m) => m.ProductFieldModule
                ),
            },
            {
              path: "tracking-and-scan",
              loadChildren: () =>
                import(
                  "./components/tracking/module/scan-and-tracking.module"
                ).then((m) => m.ScanAndTrackingModule),
            },
            {
              path: "country/list",
              loadChildren: () =>
                import("./components/country/country.module").then(
                  (m) => m.CountryModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-country",
              loadChildren: () =>
                import("./components/country/update/country-form.module").then(
                  (m) => m.CountryFormModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "city/list",
              loadChildren: () =>
                import("./components/City/city.module").then(
                  (m) => m.CityModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-city",
              loadChildren: () =>
                import("./components/City/update/city-form.module").then(
                  (m) => m.CityFormModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "product-type/list",
              loadChildren: () =>
                import("./components/product-type/product-type.module").then(
                  (m) => m.ProductTypeModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-product-type",
              loadChildren: () =>
                import(
                  "./components/product-type/update/product-type-form.module"
                ).then((m) => m.ProductTypeFormModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "service-type/list",
              loadChildren: () =>
                import("./components/service-type/service-type.module").then(
                  (m) => m.ServiceTypeModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-service-type",
              loadChildren: () =>
                import(
                  "./components/service-type/update/service-type-form.module"
                ).then((m) => m.ServiceTypeFormModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "account/list",
              loadChildren: () =>
                import("./components/accounts/account.module").then(
                  (m) => m.AccountModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-account",
              loadChildren: () =>
                import(
                  "./components/accounts/update/accounts-form.module"
                ).then((m) => m.AccountsFormModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "ticket-category/list",
              loadChildren: () =>
                import(
                  "./components/ticketCategory/ticket-category.module"
                ).then((m) => m.TicketCategoryModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-ticket-category",
              loadChildren: () =>
                import(
                  "./components/ticketCategory/update/ticket-category-form.module"
                ).then((m) => m.TicketCategoryFormModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "address-book/list",
              loadChildren: () =>
                import("./components/addressBook/address-book.module").then(
                  (m) => m.AddressBookModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-address-book",
              loadChildren: () =>
                import(
                  "./components/addressBook/update/modules/address-book-update.module"
                ).then((m) => m.AddressBookUpdateModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "ticket-sub-category/list",
              loadChildren: () =>
                import(
                  "./components/ticketSubCategory/ticket-sub-category.module"
                ).then((m) => m.TicketSubCategoryModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-ticket-sub-category",
              loadChildren: () =>
                import(
                  "./components/ticketSubCategory/update/ticket-sub-category-update.module"
                ).then((m) => m.TicketSubCategoryUpdateModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "awb-history/:id",
              loadChildren: () =>
                import("./components/awb/history/history.module").then(
                  (m) => m.HistoryModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "permissions",
              component: PermissionsComponent,
              canActivate: [AuthGuardService],
            },
            {
              path: "profile",
              component: UserProfileComponent,
              canActivate: [AuthGuardService],
            },
          ],
        },
        { path: "login", component: LoginComponent },
        { path: "verification", component: VerificationComponent },
        { path: "forgot-password", component: ForgotPasswordComponent },
        { path: "reset-password", component: NewPasswordComponent },
        { path: "access", component: AccessdeniedComponent },
        { path: "notfound", component: NotfoundComponent },
        { path: "**", redirectTo: "/notfound" },
      ],
      {
        scrollPositionRestoration: "enabled",
        anchorScrolling: "enabled",
        onSameUrlNavigation: "reload",
      }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
