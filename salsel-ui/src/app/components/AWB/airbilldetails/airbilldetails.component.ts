import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { AirbillService } from "../airbill.service";

@Component({
  selector: "app-airbilldetails",
  templateUrl: "./airbilldetails.component.html",
  styleUrls: ["./airbilldetails.component.scss"],
})
export class AirbilldetailsComponent {
  constructor(
    private activatedRoute: ActivatedRoute,
    private _airbillService: AirbillService,
    private router: Router
  ) {}
  display: any;
  singleBill: any;

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("billid");

      this.onView(a);
    });
  }

  //   On Single Ticket View
  onView(id) {
    this.display = true;
    this._airbillService.getSingleBill(id).subscribe((res) => {
      this.singleBill = res;
    });
  }
}
