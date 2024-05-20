import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { BillingViewComponent } from "./view/billing-view.component";
import { BillingComponent } from "./list/billing.component";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { AutoCompleteModule } from "primeng/autocomplete";
import { AutoFocusModule } from "primeng/autofocus";
import { AvatarModule } from "primeng/avatar";
import { AvatarGroupModule } from "primeng/avatargroup";
import { ButtonModule } from "primeng/button";
import { CalendarModule } from "primeng/calendar";
import { CascadeSelectModule } from "primeng/cascadeselect";
import { ChipsModule } from "primeng/chips";
import { DialogModule } from "primeng/dialog";
import { DropdownModule } from "primeng/dropdown";
import { ImageModule } from "primeng/image";
import { InputMaskModule } from "primeng/inputmask";
import { InputNumberModule } from "primeng/inputnumber";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { MultiSelectModule } from "primeng/multiselect";
import { PaginatorModule } from "primeng/paginator";
import { ProgressBarModule } from "primeng/progressbar";
import { RadioButtonModule } from "primeng/radiobutton";
import { RatingModule } from "primeng/rating";
import { RippleModule } from "primeng/ripple";
import { SliderModule } from "primeng/slider";
import { TableModule } from "primeng/table";
import { TagModule } from "primeng/tag";
import { ToastModule } from "primeng/toast";
import { ToggleButtonModule } from "primeng/togglebutton";
import { TooltipModule } from "primeng/tooltip";
import { SHAREDModule } from "../shared/shared.module";
import { BillingRoutingModule } from "./billing-routing.module";
import { DividerModule } from "primeng/divider";
import { InvoiceExcelDataComponent } from './list/invoice-excel-data/invoice-excel-data.component';

@NgModule({
  declarations: [BillingViewComponent,BillingComponent, InvoiceExcelDataComponent],
  imports: [
    CommonModule,
    TableModule,
    ReactiveFormsModule,
    RatingModule,
    ButtonModule,
    SliderModule,
    InputTextModule,
    ToggleButtonModule,
    RippleModule,
    MultiSelectModule,
    ProgressBarModule,
    ToastModule,
    DialogModule,
    FormsModule,
    AutoCompleteModule,
    CalendarModule,
    ChipsModule,
    ImageModule,
    DropdownModule,
    InputMaskModule,
    InputNumberModule,
    CascadeSelectModule,
    InputTextareaModule,
    RadioButtonModule,
    PaginatorModule,
    DividerModule,
    TooltipModule,
    AvatarModule,
    AvatarGroupModule,
    AutoFocusModule,
    TagModule,
    SHAREDModule,
    BillingRoutingModule
],
})
export class BillingModule {}
