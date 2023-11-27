import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { UserlistComponent } from "./userlist.component";
import { RouterModule } from "@angular/router";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: "", component: UserlistComponent }]),
  ],
  exports: [RouterModule],
})
export class UserlistroutingModule {}
