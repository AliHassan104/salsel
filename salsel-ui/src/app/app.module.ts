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
import { PermissionsModule } from "./components/permissions/permissions.module";
import { ForgotPasswordModule } from "./components/auth/forgot-password/forgot-password.module";
import { NewPasswordModule } from "./components/auth/new-password/new-password.module";
import { VerificationModule } from "./components/auth/verification/verification.module";
import { HrModuleDataComponent } from './components/hr-module/list/hr-module-data.component';
import { HrModuleFormComponent } from './components/hr-module/update/hr-module-form.component';
import { HrModuleViewComponent } from './components/hr-module/view/hr-module-view.component';

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
    VerificationModule,
    ButtonModule,
    PermissionsModule,
    ForgotPasswordModule,
    NewPasswordModule,
  ],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    AuthInterceptorProvider,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
