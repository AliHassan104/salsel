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
import { ProductService } from "./service/product.service";
import { CustomerService } from "./service/customer.service";
import { EventService } from "./service/event.service";
import { IconService } from "./service/icon.service";
import { NodeService } from "./service/node.service";
import { PhotoService } from "./service/photo.service";
import { LoginModule } from "./components/auth/login/login.module";
import { AuthInterceptorProvider } from "./components/auth/service/interceptor.service";
import { LoaderComponent } from "./components/loader/loader.component";
import { AccessdeniedComponent } from "./components/auth/accessdenied/accessdenied.component";
import { ButtonModule } from "primeng/button";
import { PermissionsModule } from "./components/permissions/permissions.module";
import { AccountListComponent } from './components/accounts/account-list/account-list.component';
import { AccountFormComponent } from './components/accounts/account-form/account-form.component';

@NgModule({
  declarations: [
    AppComponent,
    NotfoundComponent,
    LoaderComponent,
    AccessdeniedComponent,
    AccountListComponent,
    AccountFormComponent,
  ],
  imports: [
    CommonModule,
    AppRoutingModule,
    AppLayoutModule,
    LoginModule,
    ButtonModule,
    PermissionsModule,
  ],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    CustomerService,
    EventService,
    IconService,
    NodeService,
    PhotoService,
    ProductService,
    AuthInterceptorProvider,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
