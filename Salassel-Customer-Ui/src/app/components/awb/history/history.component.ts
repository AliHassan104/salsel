import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { MessageService, PrimeIcons } from "primeng/api";
import { AirbillService } from "../service/airbill.service";
@Component({
  selector: "app-history",
  templateUrl: "./history.component.html",
  styleUrls: ["./history.component.scss"],
})
export class HistoryComponent {
  events1: any[] = [];

  constructor(
    private activatedRoute: ActivatedRoute,
    private _airbillService: AirbillService,
  ) {}

  display: any;
  singleBill: any;
  id;

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      console.log(res);

      this.id = a;
      this.onView(a);
    });

    this.events1 = [
      {
        status: "AWB Created",
        date: "15/10/2020 10:30",
        color: "#9C27B0",
      },
      {
        status: "Picked Up",
        date: "15/10/2020 14:00",
        color: "#673AB7",
      },
      {
        status: "Arrived in Station",
        date: "16/10/2020 10:00",
        color: "#607D8B",
      },
      {
        status: "Held in Station",
        date: "15/10/2020 10:30",
        color: "#9C27B0",
      },
      {
        status: "Depart from Station",
        date: "15/10/2020 16:15",
        color: "#FF9800",
      },
      {
        status: "Arrived in Hub",
        date: "15/10/2020 16:15",
        color: "#FF9800",
      },
      {
        status: "Depart from Hub",
        date: "15/10/2020 14:00",
        color: "#673AB7",
      },

      {
        status: "Out for Delivery",
        date: "16/10/2020 10:00",
        color: "#607D8B",
      },
      {
        status: "Delivered",
        date: "16/10/2020 10:00",
        color: "#607D8B",
      },
    ];
  }

  onView(id) {
    this.display = true;
    this._airbillService.getSingleBill(id).subscribe((res) => {
      this.singleBill = res;
    });
  }
}
