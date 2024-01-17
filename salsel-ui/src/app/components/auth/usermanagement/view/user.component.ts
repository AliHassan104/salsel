import { Component, OnInit } from "@angular/core";
import { IUser } from "../model/userDto";
import { UserService } from "../service/user.service";
import { ActivatedRoute, Router } from "@angular/router";

@Component({
  selector: "app-user",
  templateUrl: "./user.component.html",
  styleUrls: ["./user.component.scss"],
})
export class UserComponent implements OnInit {
  user?: IUser;
  id?: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private UserService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.paramsSetup();
  }

  paramsSetup() {
    this.activatedRoute.paramMap.subscribe((res: any) => {
      this.id = res.get("id");
      this.getSingleUser(this.id);
    });
  }

  getSingleUser(id: any) {
    this.UserService.GetUserById(id).subscribe((res: any) => {
      this.user = res;
    });
  }
}
