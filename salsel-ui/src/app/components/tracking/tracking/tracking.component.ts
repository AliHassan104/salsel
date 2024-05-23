import { Component, ElementRef, ViewChild } from "@angular/core";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { AirbillService } from "../../awb/service/airbill.service";
import { DatePipe } from "@angular/common";
import { Table } from "primeng/table";

declare var onScan: any;

@Component({
  selector: "app-tracking",
  templateUrl: "./tracking.component.html",
  styleUrls: ["./tracking.component.scss"],
  providers: [MessageService, DatePipe],
})
export class TrackingComponent {
  trackingNumber;
  trackingMode: boolean = false;
  uniqueScanNum;
  trackingNumbers = [];
  airBills = [];

  history: any[] = [];
  display: any;
  singleBill: any;
  updatedStatuses: any = {};
  selectedStatus;
  id;

  loading: any;

  @ViewChild("filter") filter!: ElementRef;
  @ViewChild("beepSound") beepSound: ElementRef<HTMLAudioElement>;
  @ViewChild("trackingField") trackingField: ElementRef;
  @ViewChild("singleTrackingField") singleTrackingField: ElementRef;

  constructor(
    private messageService: MessageService,
    private dropdownService: DropdownService,
    private _airbillService: AirbillService,
    private formService: FormvalidationService,
    private datePipe: DatePipe
  ) {
    onScan.attachTo(document, {
      onScan: (sScanned, iQty) => {
        console.log("Scanned:", iQty + "x " + sScanned);
        this.trackingNumber = sScanned;
        this.beep();
        this.getShippingLatestData(sScanned);
      },
    });
  }

  onClickTracking(trackingNumber: any) {
    window.scrollTo({ top: 0, behavior: "smooth" });
    this.onGettingUniqueNum(trackingNumber);
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

  //   For table filtering purpose
  onGlobalFilter(table: Table, event: any) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  beep() {
    this.beepSound?.nativeElement?.play();
  }

  onGettingUniqueNum(uniqueNumber: any) {
    this.display = true;
    this._airbillService
      .getSingleBillByUniqueNumber(uniqueNumber)
      .subscribe((res: any) => {
        this.singleBill = res;
        this.getTrackingHistory(res?.id);
      });
  }

  getShippingLatestData(multipleTracking: any) {
    this.airBills = []
    const values = multipleTracking
      .split(/[\n\s,]+/)
      .map((value) => value.trim())
      .filter((value) => value)
      .map((value) => {
        const num = Number(value);
        return isNaN(num) ? null : num;
      })
      .filter((value) => value !== null);

    this.trackingNumbers = values;

    this._airbillService
      .getShippingByTrackingNumbers(this.trackingNumbers)
      .subscribe(
        (res: any[]) => {
          if (res != null && res.length > 0) {
            console.log(res);

            res.forEach((awb: any) => {
              // Check if the AWB already exists in the list
              const exists = this.airBills.some(
                (existingAwb) => existingAwb.id === awb.id
              );
              if (!exists) {
                this.airBills.push(awb);
                this.trackingField.nativeElement.value = "";
              } else {
                this.messageService.add({
                  severity: "warn",
                  summary: "Warning",
                  detail: "Tracking number already exists",
                });
                this.trackingField.nativeElement.value = "";
              }
            });
          } else {
            this.messageService.add({
              severity: "error",
              summary: "Error",
              detail: "No Airbill Found For this tracking Number",
            });
            this.trackingField.nativeElement.value = "";
          }
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

  onRemoveAll(){
    this.airBills = [];
  }

  onTrackTrackingNumber(multipleTracking: any, singleTracking: any) {
    if (multipleTracking) {
      this.getShippingLatestData(multipleTracking);
    } else if (singleTracking) {
      this.trackingNumber = singleTracking;
      if (this.trackingNumber != "") {
        this.onGettingUniqueNum(this.trackingNumber);
        this.getShippingLatestData(this.trackingNumber);
        this.singleTrackingField.nativeElement.value = "";
      }
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
