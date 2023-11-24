import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { UserComponent } from "./user.component";
import { CheckboxModule } from "primeng/checkbox";
import { FormsModule } from "@angular/forms";
import { UserRoutingModule } from "./user-routing.module";
@NgModule({
  declarations: [UserComponent],
  imports: [CommonModule, CheckboxModule, FormsModule, UserRoutingModule],
})
export class UserModule {}
