import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { UserlistComponent } from "./userlist/userlist.component";
import { UserComponent } from "./user/user.component";
import { Route, RouterModule } from "@angular/router";
import { AddUserComponent } from "./add-user/add-user.component";

const routes: Route[] = [
  { path: "", component: UserlistComponent },
  { path: "user", component: UserComponent },
  { path: "adduser", component: AddUserComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class UsermanagroutingModule {}
