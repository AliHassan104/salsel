import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { TicktingService } from "src/app/demo/service/tickting.service";

@Component({
  selector: "app-ticketitem",
  templateUrl: "./ticketitem.component.html",
  styleUrls: ["./ticketitem.component.scss"],
})
export class TicketitemComponent implements OnInit {
  constructor(
    private activatedRoute: ActivatedRoute,
    private _ticketService: TicktingService
  ) {}
  display: any;
  sTicket: any;
  singleTicket;

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      console.log(a);

      this.onView(a);
    });
  }

  //   On Single Ticket View
  onView(id) {
    this.display = true;
    this._ticketService.getSingleTicket(id).subscribe((res) => {
      this.sTicket = res;
      this.singleTicket = this.sTicket.note;
      console.log(this.singleTicket);
    });
    console.log(id);
  }
}
