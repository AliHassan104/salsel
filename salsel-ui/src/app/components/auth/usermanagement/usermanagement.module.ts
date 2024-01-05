import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CheckboxModule } from "primeng/checkbox";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TableModule } from "primeng/table";
import { FileUploadModule } from "primeng/fileupload";
import { ButtonModule } from "primeng/button";
import { RippleModule } from "primeng/ripple";
import { ToastModule } from "primeng/toast";
import { ToolbarModule } from "primeng/toolbar";
import { RatingModule } from "primeng/rating";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { DropdownModule } from "primeng/dropdown";
import { RadioButtonModule } from "primeng/radiobutton";
import { InputNumberModule } from "primeng/inputnumber";
import { DialogModule } from "primeng/dialog";
import { TagModule } from "primeng/tag";
import { UsermanagroutingModule } from "./usermanagrouting.module";
import { UserlistComponent } from "./list/userlist.component";
import { UserComponent } from "./view/user.component";
import { PasswordModule } from "primeng/password";
import { TooltipModule } from "primeng/tooltip";
import { AvatarModule } from "primeng/avatar";
import { UserProfileModule } from "./user-profile/user-profile.module";

@NgModule({
  declarations: [UserlistComponent, UserComponent],
  imports: [
    CommonModule,
    CheckboxModule,
    FormsModule,
    ReactiveFormsModule,
    UsermanagroutingModule,
    TableModule,
    FileUploadModule,
    ButtonModule,
    RippleModule,
    ToastModule,
    ToolbarModule,
    RatingModule,
    InputTextModule,
    InputTextareaModule,
    DropdownModule,
    RadioButtonModule,
    InputNumberModule,
    DialogModule,
    TagModule,
    PasswordModule,
    TooltipModule,
    AvatarModule,
    UserProfileModule,
  ],
})
export class UsermanagementModule {}
