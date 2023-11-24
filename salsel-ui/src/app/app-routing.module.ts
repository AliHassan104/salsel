import { RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { NotfoundComponent } from "./demo/components/notfound/notfound.component";
import { AppLayoutComponent } from "./layout/app.layout.component";

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
                import("./demo/components/dashboard/dashboard.module").then(
                  (m) => m.DashboardModule
                ),
            },
            {
              path: "user",
              loadChildren: () =>
                import("./customComponents/user/user.module").then(
                  (m) => m.UserModule
                ),
            },
            {
              path: "addticket",
              loadChildren: () =>
                import("./customComponents/ticketform/ticketform.module").then(
                  (m) => m.TicketformModule
                ),
            },
            {
              path: "tickets",
              loadChildren: () =>
                import(
                  "./customComponents/ticketsdata/ticketsdata.module"
                ).then((m) => m.TicketsdataModule),
            },
            {
              path: "tickets/:id",
              loadChildren: () =>
                import("./customComponents/ticketitem/ticketitem.module").then(
                  (m) => m.TicketitemModule
                ),
            },

            {
              path: "utilities",
              loadChildren: () =>
                import("./demo/components/utilities/utilities.module").then(
                  (m) => m.UtilitiesModule
                ),
            },
          ],
        },
        {
          path: "auth",
          loadChildren: () =>
            import("./demo/components/auth/auth.module").then(
              (m) => m.AuthModule
            ),
        },
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
