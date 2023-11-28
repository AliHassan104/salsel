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
              path: "tickets/addticket",
              loadChildren: () =>
                import(
                  "./components/Tickets/ticketform/ticketform.module"
                ).then((m) => m.TicketformModule),
            },
            {
              path: "tickets",
              loadChildren: () =>
                import(
                  "./components/Tickets/ticketsdata/ticketsdata.module"
                ).then((m) => m.TicketsdataModule),
            },
            {
              path: "tickets/addticket/:id",
              loadChildren: () =>
                import(
                  "./components/Tickets/ticketitem/ticketitem.module"
                ).then((m) => m.TicketitemModule),
            },
            {
              path: "awbcreation",
              loadChildren: () =>
                import("./components/awbcreation/awbcreation.module").then(
                  (m) => m.AwbcreationModule
                ),
            },
            { path: 'department', 
              loadChildren: () => 
                import('./components/department/department.module')
                .then(m => m.DepartmentModule) 
            },
            { path: 'department-category', 
              loadChildren: () => 
                import('./components/department-category/department-category.module')
                .then(m => m.DepartmentCategoryModule) 
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
