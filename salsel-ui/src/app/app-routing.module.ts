import { RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { NotfoundComponent } from "./components/notfound/notfound.component";
import { AppLayoutComponent } from "./layout/app.layout.component";
import { LoginComponent } from "./components/auth/login/login.component";
import { UserlistModule } from "./components/auth/userlist/userlist.module";

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: "",
          component: AppLayoutComponent,
          children: [
            {
              path: "",
              loadChildren: () =>
                import("./components/dashboard/dashboard.module").then(
                  (m) => m.DashboardModule
                ),
            },
            {
              path: "user",
              loadChildren: () =>
                import("./components/user/user.module").then(
                  (m) => m.UserModule
                ),
            },
            {
              path: "tickets",
              loadChildren: () =>
                import("./components/Tickets/tickets.module").then(
                  (m) => m.TicketsModule
                ),
            },
            {
              path: "airwaybills",
              loadChildren: () =>
                import("./components/airbill/airbill.module").then(
                  (m) => m.AirbillModule
                ),
            },
            {
              path: "department",
              loadChildren: () =>
                import("./components/department/department.module").then(
                  (m) => m.DepartmentModule
                ),
            },
            {
              path: "department-category",
              loadChildren: () =>
                import(
                  "./components/department-category/department-category.module"
                ).then((m) => m.DepartmentCategoryModule),
            },
            {
              path: "userslist",
              loadChildren: () =>
                import("./components/auth/userlist/userlist.module").then(
                  (m) => m.UserlistModule
                ),
            },
          ],
        },
        { path: "login", component: LoginComponent },
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
