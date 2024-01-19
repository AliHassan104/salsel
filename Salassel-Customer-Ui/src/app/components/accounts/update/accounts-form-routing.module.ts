import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Route, RouterModule } from "@angular/router";
import { AccountFormComponent } from "./account-form.component";

const routes: Route[] = [
  {
    path: "",
    component: AccountFormComponent,
  },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AccountsFormRoutingModule {}
