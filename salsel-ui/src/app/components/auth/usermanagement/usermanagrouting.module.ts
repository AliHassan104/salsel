import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { UserlistComponent } from "./list/userlist.component";
import { UserComponent } from "./view/user.component";
import { Route, RouterModule } from "@angular/router";
import { AddUserComponent } from "./update/add-user.component";

const routes: Route[] = [
  { path: "", component: UserlistComponent },
  { path: ":id", component: UserComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class UsermanagroutingModule {}
