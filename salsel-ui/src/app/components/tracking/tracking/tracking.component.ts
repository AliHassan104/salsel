import { Component, ElementRef, ViewChild } from "@angular/core";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { AirbillService } from "../../awb/service/airbill.service";

declare var onScan: any;

@Component({
  selector: "app-tracking",
  templateUrl: "./tracking.component.html",
  styleUrls: ["./tracking.component.scss"],
  providers: [MessageService],
})
export class TrackingComponent {
  trackingNumber;
  trackingMode: boolean = false;
  uniqueScanNum;

  history: any[] = [];
  display: any;
  singleBill: any;
  id;

  @ViewChild("beepSound") beepSound: ElementRef<HTMLAudioElement>;

  constructor(
    private messageService: MessageService,
    private dropdownService: DropdownService,
    private _airbillService: AirbillService,
    private formService: FormvalidationService
  ) {
    onScan.attachTo(document, {
      onScan: (sScanned, iQty) => {
        console.log("Scanned:", iQty + "x " + sScanned);
        this.trackingNumber = sScanned;
        this.beep();
        this.onGettingUniqueNum(sScanned);
      },
    });
  }

  getTrackingHistory(id: any) {
    this._airbillService.getBillTrackingHistory({ awbId: id }).subscribe(
      (res: any) => {
        this.history = res;
        console.log(res);

        this.history = this.history.reverse();
        this.trackingMode = true;
      },
      (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: error?.error?.error,
        });
      }
    );
  }

  beep(){
    this.beepSound?.nativeElement?.play();
  }

  onGettingUniqueNum(uniqueNumber: any) {
    this.display = true;
    this._airbillService.getSingleBillByUniqueNumber(uniqueNumber).subscribe(
      (res: any) => {
        this.singleBill = res;
        this.getTrackingHistory(res?.id);

      },
      (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: error?.error?.error,
        });
      }
    );
  }

  onTrackTrackingNumber(value:any) {
this.trackingNumber = value;
    if (this.trackingNumber != "") {
      this.onGettingUniqueNum(this.trackingNumber);
    } else {
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Please insert a tracking number",
      });
    }
  }

  onRefresh() {
    this.onGettingUniqueNum(this.trackingNumber);
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

  onBack() {
    this.trackingMode = false;
    this.trackingNumber = null;
  }

  ngOnDestroy(): void {
    onScan.detachFrom(document);
  }
}
