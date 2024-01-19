import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { AirbillService } from "../service/airbill.service";
import { MessageService } from "primeng/api";

@Component({
  selector: "app-airbilldetails",
  templateUrl: "./airbilldetails.component.html",
  styleUrls: ["./airbilldetails.component.scss"],
  providers: [MessageService],
})
export class AirbilldetailsComponent implements OnInit {
  constructor(
    private activatedRoute: ActivatedRoute,
    private _airbillService: AirbillService,
    private router: Router,
    private messageService: MessageService
  ) {}
  display: any;
  singleBill: any;
  id;

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res?: any) => {
      var a = res.get("billid");
      this.id = a;
      this.onView(a);
    });
  }

  downloadAwb() {
    this._airbillService.downloadBill(this.id).subscribe(
      (res: any) => {
        this.downloadSuccess();
        this._airbillService.downloadFile(
          res,
          `Awb_${this.singleBill?.uniqueNumber}.pdf`
        );
      },
      (error) => {
        this.downloadError();
      }
    );
  }

  //   On Single Ticket View
  onView(id) {
    this.display = true;
    this._airbillService.getSingleBill(id).subscribe((res?: any) => {
      this.singleBill = res;
    });
  }

  downloadError() {
    this.messageService.add({
      severity: "error",
      summary: "Error",
      detail: "Download Failed",
    });
  }

  downloadSuccess() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "File Successfully Downloaded",
    });
  }
}
