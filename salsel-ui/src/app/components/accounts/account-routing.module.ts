import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AccountFormComponent } from "./account-form/account-form.component";
import { AccountListComponent } from "./account-list/account-list.component";
import { Route, RouterModule } from "@angular/router";

const routes: Route[] = [
  { path: "", component: AccountListComponent },
  { path: "addcountry", component: AccountFormComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AccountRoutingModule {}
