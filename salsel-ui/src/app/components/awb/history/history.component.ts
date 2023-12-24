import { Component } from "@angular/core";
import { PrimeIcons } from "primeng/api";
@Component({
  selector: "app-history",
  templateUrl: "./history.component.html",
  styleUrls: ["./history.component.scss"],
})
export class HistoryComponent {
  events1: any[] = [];

  events2: any[] = [];

  ngOnInit() {
    this.events1 = [
      {
        status: "AWB Created",
        date: "15/10/2020 10:30",
        icon: PrimeIcons.FILE,
        color: "#9C27B0",
        image: "paper_2.png",
      },
      {
        status: "Picked Up",
        date: "15/10/2020 14:00",
        icon: PrimeIcons.SHOPPING_CART,
        color: "#673AB7",
        image: "3697568.png",
      },
      {
        status: "Arrived in Station",
        date: "16/10/2020 10:00",
        icon: PrimeIcons.SIGN_IN,
        color: "#607D8B",
        image: "8085.png",
      },
      {
        status: "Held in Station",
        date: "15/10/2020 10:30",
        icon: PrimeIcons.CLOCK,
        color: "#9C27B0",
        image: "8085.png",
      },
      {
        status: "Depart from Station",
        date: "15/10/2020 16:15",
        icon: PrimeIcons.SIGN_OUT,
        color: "#FF9800",
        image: "6333.png",
      },
      {
        status: "Arrived in Hub",
        date: "15/10/2020 16:15",
        icon: PrimeIcons.SIGN_IN,
        color: "#FF9800",
        image: "8085.png",
      },
      {
        status: "Depart from Hub",
        date: "15/10/2020 14:00",
        icon: PrimeIcons.SIGN_OUT,
        color: "#673AB7",
        condition: "",
        image: "6333.png",
      },

      {
        status: "Out for Delivery",
        date: "16/10/2020 10:00",
        icon: PrimeIcons.TRUCK,
        color: "#607D8B",
        image: "Loading workman carrying boxes.png",
      },
      {
        status: "Delivered",
        date: "16/10/2020 10:00",
        icon: PrimeIcons.CHECK,
        color: "#607D8B",
        image: "6233230.png",
      },
    ];

    this.events2 = ["2020", "2021", "2022", "2023"];
  }
}
