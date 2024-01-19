import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Route, RouterModule } from "@angular/router";
import { AddUserComponent } from "./add-user.component";

const routes: Route[] = [{ path: "", component: AddUserComponent }];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class UserFormRoutingModule {}
