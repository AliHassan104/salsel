import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AirbilldetailsComponent } from "./airbilldetails.component";
import { RouterModule } from "@angular/router";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: "", component: AirbilldetailsComponent }]),
  ],
  exports: [RouterModule],
})
export class AirbilldetailsroutingModule {}
