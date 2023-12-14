import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { UserlistComponent } from "./userlist/userlist.component";
import { UserComponent } from "./user/user.component";
import { Route, RouterModule } from "@angular/router";
import { AddUserComponent } from "./add-user/add-user.component";

const routes: Route[] = [
  { path: "", component: AddUserComponent },
  { path: "list", component: UserlistComponent },
  { path: "list/:id", component: UserComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class UsermanagroutingModule {}
