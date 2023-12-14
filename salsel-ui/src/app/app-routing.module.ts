import { RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { NotfoundComponent } from "./components/notfound/notfound.component";
import { AppLayoutComponent } from "./layout/app.layout.component";
import { LoginComponent } from "./components/auth/login/login.component";
import { AccessdeniedComponent } from "./components/auth/accessdenied/accessdenied.component";
import { PermissionsComponent } from "./components/permissions/permissions.component";
import { AuthGuardService } from "./components/auth/service/auth-guard.service";

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
              path: "user",
              loadChildren: () =>
                import(
                  "./components/auth/usermanagement/usermanagement.module"
                ).then((m) => m.UsermanagementModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "ticket",
              loadChildren: () =>
                import("./components/Tickets/tickets.module").then(
                  (m) => m.TicketsModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "awb",
              loadChildren: () =>
                import("./components/awb/airbill.module").then(
                  (m) => m.AirbillModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "department",
              loadChildren: () =>
                import("./components/department/department.module").then(
                  (m) => m.DepartmentModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "department-category",
              loadChildren: () =>
                import(
                  "./components/department-category/department-category.module"
                ).then((m) => m.DepartmentCategoryModule),
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
              path: "country",
              loadChildren: () =>
                import("./components/country/country.module").then(
                  (m) => m.CountryModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "city",
              loadChildren: () =>
                import("./components/City/city.module").then(
                  (m) => m.CityModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "product-type",
              loadChildren: () =>
                import("./components/product-type/product-type.module").then(
                  (m) => m.ProductTypeModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "service-type",
              loadChildren: () =>
                import("./components/service-type/service-type.module").then(
                  (m) => m.ServiceTypeModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "account",
              loadChildren: () =>
                import("./components/accounts/account.module").then(
                  (m) => m.AccountModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "permissions",
              component: PermissionsComponent,
              canActivate: [AuthGuardService],
            },
          ],
        },
        { path: "login", component: LoginComponent },
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
