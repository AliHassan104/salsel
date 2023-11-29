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
import { CountryService } from "./service/country.service";
import { CustomerService } from "./service/customer.service";
import { EventService } from "./service/event.service";
import { IconService } from "./service/icon.service";
import { NodeService } from "./service/node.service";
import { PhotoService } from "./service/photo.service";
import { LoginModule } from "./components/auth/login/login.module";

import { AuthInterceptorProvider } from "./components/auth/service/logininterceptor.service";
import { LoaderComponent } from "./components/loader/loader.component";

@NgModule({
  declarations: [AppComponent, NotfoundComponent, LoaderComponent],
  imports: [CommonModule, AppRoutingModule, AppLayoutModule, LoginModule],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    CountryService,
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
