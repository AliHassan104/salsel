import { NgModule } from "@angular/core";
import { HashLocationStrategy, LocationStrategy } from "@angular/common";
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
import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { LogininterceptorService } from "./components/auth/logininterceptor.service";

@NgModule({
  declarations: [AppComponent, NotfoundComponent],
  imports: [AppRoutingModule, AppLayoutModule, LoginModule],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    CountryService,
    CustomerService,
    EventService,
    IconService,
    NodeService,
    PhotoService,
    ProductService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LogininterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
