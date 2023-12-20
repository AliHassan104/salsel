import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { AirbillService } from "../service/airbill.service";

@Component({
  selector: "app-airbilldetails",
  templateUrl: "./airbilldetails.component.html",
  styleUrls: ["./airbilldetails.component.scss"],
})
export class AirbilldetailsComponent implements OnInit {
  constructor(
    private activatedRoute: ActivatedRoute,
    private _airbillService: AirbillService,
    private router: Router
  ) {}
  display: any;
  singleBill: any;
  id;

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("billid");
      this.id = a;
      this.onView(a);
    });
  }

  downloadAwb() {
    this._airbillService.downloadBill(this.id).subscribe((res) => {
      if (res) {
        console.log("success");
      }
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
