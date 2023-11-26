import { RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { NotfoundComponent } from "./components/notfound/notfound.component";
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
              path: "addticket",
              loadChildren: () =>
                import("./components/ticketform/ticketform.module").then(
                  (m) => m.TicketformModule
                ),
            },
            {
              path: "tickets",
              loadChildren: () =>
                import("./components/ticketsdata/ticketsdata.module").then(
                  (m) => m.TicketsdataModule
                ),
            },
            {
              path: "tickets/:id",
              loadChildren: () =>
                import("./components/ticketitem/ticketitem.module").then(
                  (m) => m.TicketitemModule
                ),
            },
          ],
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
