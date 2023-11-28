import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AirbilldetailsComponent } from "./airbilldetails.component";
import { DialogModule } from "primeng/dialog";
import { ButtonModule } from "primeng/button";
import { AirbilldetailsroutingModule } from "./airbilldetailsrouting.module";

@NgModule({
  declarations: [AirbilldetailsComponent],
  imports: [
    CommonModule,
    AirbilldetailsroutingModule,
    DialogModule,
    ButtonModule,
  ],
})
export class AirbilldetailsModule {}
