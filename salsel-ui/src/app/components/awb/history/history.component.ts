import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { MessageService, PrimeIcons } from "primeng/api";
import { AirbillService } from "../service/airbill.service";
@Component({
  selector: "app-history",
  templateUrl: "./history.component.html",
  styleUrls: ["./history.component.scss"],
  providers: [MessageService],
})
export class HistoryComponent {
  history: any[] = [];

  constructor(
    private activatedRoute: ActivatedRoute,
    private _airbillService: AirbillService
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

    this.getTrackingHistory();
  }

  getTrackingHistory() {
    this._airbillService
      .getBillTrackingHistory({ awbId: this.id })
      .subscribe((res: any) => {
        console.log(res);
        this.history = res;
        this.history = this.history.reverse();
      });
  }

  getIcon(status: any): string {
    switch (status) {
      case "AWB Created":
        return "pi pi-credit-card";
      case "Picked Up":
        return "pi pi-shopping-bag";
      case "Arrived in Station":
        return "pi pi-car";
      case "Held in Station":
        return "pi pi-building";
      case "Depart from Station":
        return "pi pi-car";
      case "Arrived in Hub":
        return "pi pi-building";
      case "Depart from Hub":
        return "pi pi-car";
      case "Out for Delivery":
        return "pi pi-truck";
      case "Delivered":
        return "pi pi-check";
      default:
        return "";
    }
  }

  getColor(status: any): string {
    switch (status) {
      case "AWB Created":
        return "#9C27B0";
      case "Picked Up":
        return "#673AB7";
      case "Arrived in Station":
        return "#FF9800";
      case "Held in Station":
        return "#607D8B";
      case "Depart from Station":
        return "#9C27B0";
      case "Arrived in Hub":
        return "#673AB7";
      case "Depart from Hub":
        return "#FF9800";
      case "Out for Delivery":
        return "#607D8B";
      case "Delivered":
        return "#9C27B0";
      default:
        return "";
    }
  }

  getImage(status: any): string {
    switch (status) {
      case "AWB Created":
        return "paper_2.png";
      case "Picked Up":
        return "3697568.png";
      case "Arrived in Station":
        return "6333.png";
      case "Held in Station":
        return "8085.png";
      case "Depart from Station":
        return "3697568.png";
      case "Arrived in Hub":
        return "8085.png";
      case "Depart from Hub":
        return "3697568.png";
      case "Out for Delivery":
        return "Loading workman carrying boxes.png";
      case "Delivered":
        return "6233230.png";
      default:
        return "";
    }
  }

  onView(id) {
    this.display = true;
    this._airbillService.getSingleBill(id).subscribe((res) => {
      this.singleBill = res;
    });
  }
}
