import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AccountFormComponent } from "./account-form/account-form.component";
import { AccountListComponent } from "./account-list/account-list.component";
import { Route, RouterModule } from "@angular/router";
import { AccountViewComponent } from "./account-view/account-view.component";

const routes: Route[] = [
  { path: "", component: AccountListComponent },
  { path: "addaccount", component: AccountFormComponent },
  { path: ":id", component: AccountViewComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AccountRoutingModule {}
