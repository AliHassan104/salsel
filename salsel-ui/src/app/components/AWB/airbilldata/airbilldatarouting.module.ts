import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AirbilldataComponent } from "./airbilldata.component";
import { RouterModule } from "@angular/router";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: "", component: AirbilldataComponent }]),
  ],
  exports: [RouterModule],
})
export class AirbilldataroutingModule {}
