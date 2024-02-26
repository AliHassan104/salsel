import { NgModule } from "@angular/core";
import {
  CommonModule,
  HashLocationStrategy,
  LocationStrategy,
} from "@angular/common";
import { AppComponent } from "./app.component";
import { AppRoutingModule } from "./app-routing.module";
import { AppLayoutModule } from "./layout/app.layout.module";
import { NotfoundComponent } from "./components/notfound/notfound.component";
import { LoginModule } from "./components/auth/login/login.module";
import { AuthInterceptorProvider } from "./components/auth/service/interceptor.service";
import { LoaderComponent } from "./components/loader/loader.component";
import { AccessdeniedComponent } from "./components/auth/accessdenied/accessdenied.component";
import { ButtonModule } from "primeng/button";
import { ForgotPasswordModule } from "./components/auth/forgot-password/forgot-password.module";
import { NewPasswordModule } from "./components/auth/new-password/new-password.module";
import { VerificationModule } from "./components/auth/verification/verification.module";

@NgModule({
  declarations: [
    AppComponent,
    NotfoundComponent,
    LoaderComponent,
    AccessdeniedComponent,
  ],
  imports: [
    CommonModule,
    AppRoutingModule,
    AppLayoutModule,
    LoginModule,
    ButtonModule,
    ForgotPasswordModule,
    NewPasswordModule,
    VerificationModule
  ],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    AuthInterceptorProvider,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
