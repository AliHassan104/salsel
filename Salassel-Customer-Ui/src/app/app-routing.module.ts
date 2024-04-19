import { RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { NotfoundComponent } from "./components/notfound/notfound.component";
import { AppLayoutComponent } from "./layout/app.layout.component";
import { LoginComponent } from "./components/auth/login/login.component";
import { AccessdeniedComponent } from "./components/auth/accessdenied/accessdenied.component";
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
              path: "create-user",
              loadChildren: () =>
                import(
                  "./components/auth/usermanagement/update/user-form.module"
                ).then((m) => m.UserFormModule),
              canActivate: [AuthGuardService],
            },
            {
              path: "booking/list",
              loadChildren: () =>
                import("./components/Booking/booking.module").then(
                  (m) => m.BookingModule
                ),
              canActivate: [AuthGuardService],
            },
            {
              path: "create-booking",
              loadChildren: () =>
                import("./components/Booking/update/booking-form.module").then(
                  (m) => m.BookingFormModule
                ),
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
              path: "awb-history/:id",
              loadChildren: () =>
                import("./components/awb/history/history.module").then(
                  (m) => m.HistoryModule
                ),
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
