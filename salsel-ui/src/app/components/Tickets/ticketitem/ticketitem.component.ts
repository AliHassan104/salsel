import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { TicktingService } from "src/app/components/Tickets/tickting.service";
import { AirbillService } from "../../airbill/airbill.service";

@Component({
  selector: "app-ticketitem",
  templateUrl: "./ticketitem.component.html",
  styleUrls: ["./ticketitem.component.scss"],
})
export class TicketitemComponent implements OnInit {
  constructor(
    private activatedRoute: ActivatedRoute,
    private _ticketService: TicktingService,
    private router: Router,
    private _airbillService: AirbillService
  ) {}
  display: any;
  singleTicket: any;

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");

      this.onView(a);
    });
  }

  //   On Single Ticket View
  onView(id) {
    this.display = true;
    this._ticketService.getSingleTicket(id).subscribe((res) => {
      this.singleTicket = res;
    });
  }

  createAwb() {
    this.router.navigate(["airwaybills/createairbill"]);
    this._airbillService.createBillThrTickId.next(true);
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      this._airbillService.createBillThrTickId.next(a);
    });
  }
}
