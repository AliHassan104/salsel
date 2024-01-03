import { Component, OnInit } from "@angular/core";
import { UserService } from "../service/user.service";
import { IUser } from "../model/userDto";

@Component({
  selector: "app-user-profile",
  templateUrl: "./user-profile.component.html",
  styleUrls: ["./user-profile.component.scss"],
})
export class UserProfileComponent implements OnInit {
  userEmail?;
  activeUser?: IUser;
  editMode?: boolean = false;
  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userEmail = localStorage.getItem("loginUserEmail");

    this.getActiveUser();
  }

  getActiveUser() {
    this.userService.getUserByEmail(this.userEmail).subscribe((res: any) => {
      this.activeUser = res;
      console.log(this.activeUser);
    });
  }
}
