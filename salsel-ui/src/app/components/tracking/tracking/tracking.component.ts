import { Component, ElementRef, ViewChild } from "@angular/core";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { AirbillService } from "../../awb/service/airbill.service";
import { DatePipe } from "@angular/common";
import { Table } from "primeng/table";
import { TrackingService } from "../service/tracking-scan.service";
import { Router } from "@angular/router";
import { Dropdown } from "primeng/dropdown";

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
  latestData: any;
  dataAwb: any;
  statusToSend: any;
  productFields: any;
  selectedStatusDropdown: any;
  loading: any;

  @ViewChild("filter") filter!: ElementRef;
  @ViewChild("beepSound") beepSound: ElementRef<HTMLAudioElement>;
  @ViewChild("trackingField") trackingField: ElementRef;
  @ViewChild("singleTrackingField") singleTrackingField: ElementRef;
    uniqueNumberForExcel: any;
  constructor(
    private messageService: MessageService,
    private trackingService: TrackingService,
    private _airbillService: AirbillService,
    private router: Router,
    private dropdownService: DropdownService
  ) {
    onScan.attachTo(document, {
      onScan: (sScanned, iQty) => {
        console.log("Scanned:", iQty + "x " + sScanned);
        this.trackingNumber = sScanned;
        this.beep();
        this.getShippingLatestData(sScanned, "");
      },
    });
  }

  ngOnInit() {
    this.getAllProductField();
  }

  getAllProductField() {
    this.dropdownService.getAllProductFields().subscribe((res: any) => {
      this.productFields = res;

      this.statusToSend = this.dropdownService.extractNames(
        this.productFields.filter(
          (data: any) => data?.name == "AirbillStatus"
        )[0].productFieldValuesList
      );
    });
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
      });
  }

  getTrackingHistory(id: any,trackingNumber:any) {
    console.log(trackingNumber);

    this._airbillService.getBillTrackingHistory({ awbId: id }).subscribe(
      (res: any) => {
        this.history = res;
        this.latestData = res[res.length - 1];
        console.log(this.history);
        console.log(this.latestData);
        this.uniqueNumberForExcel = trackingNumber

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

  getShippingLatestData(multipleTracking: any, awbStatus: any) {
    this.airBills = [];
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
      .getShippingByTrackingNumbers(this.trackingNumbers, awbStatus)
      .subscribe(
        (res: any) => {
          if (res != null && res.length > 1) {
            console.log(res);
            this.selectedStatus = null;
            res.forEach((awb: any) => {
              // Check if the AWB already exists in the list
              const exists = this.airBills.some(
                (existingAwb) => existingAwb.id === awb.id
              );
              if (!exists) {
                this.airBills.push(awb);
                this.trackingField.nativeElement.value = "";
                this.selectedStatusDropdown = null;
              } else {
                this.messageService.add({
                  severity: "warn",
                  summary: "Warning",
                  detail: "Tracking number already exists",
                });
                this.selectedStatusDropdown = null;
                this.trackingField.nativeElement.value = "";
              }
            });
          } else if (res.length == 1) {
            this.trackingMode = true;
            console.log(res);

            this.selectedStatusDropdown = null;
            if (res) {
              this.airBills.push(res[0]);
              this.getTrackingHistory(res[0]?.awb?.id, res[0]?.awb?.uniqueNumber);
            }
          } else {
            this.messageService.add({
              severity: "error",
              summary: "Error",
              detail: "No Airbill Found For this tracking Number",
            });
            this.selectedStatusDropdown = null;
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

  onRemoveAll() {
    this.airBills = [];
    this.trackingNumbers = null;
  }

  onDownloadEmployeeExcel() {
    this.trackingService
      .downloadTrackingDataInExcel(this.trackingNumbers)
      .subscribe(
        (res: any) => {
          this._airbillService.downloadExcelFile(res, "Tracking_History.xlsx");
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Download Successfull",
          });
        },
        (error) => {
          this.messageService.add({
            severity: "error",
            summary: "Error",
            detail: "No Airbill found",
          });
        }
      );
  }

  onDownloadAwbExcel() {
    this.trackingService
      .downloadAwbDataInExcel(this.uniqueNumberForExcel)
      .subscribe(
        (res: any) => {
          this._airbillService.downloadExcelFile(res, "Awb_Detail.xlsx");
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Download Successfull",
          });
        },
        (error) => {
          this.messageService.add({
            severity: "error",
            summary: "Error",
            detail: "No Airbill found",
          });
        }
      );
  }

  onDownloadHistoryExcel() {
    this.trackingService
      .downloadHistoryDataInExcel(this.uniqueNumberForExcel)
      .subscribe(
        (res: any) => {
          this._airbillService.downloadExcelFile(
            res,
            `Tracking_History_${this.uniqueNumberForExcel}.xlsx`
          );
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Download Successfull",
          });
        },
        (error) => {
          this.messageService.add({
            severity: "error",
            summary: "Error",
            detail: "No Airbill found",
          });
        }
      );
  }

  onTrackTrackingNumber(
    multipleTracking: any,
    singleTracking: any,
    awbStatus: any
  ) {
    if (multipleTracking) {
      this.getShippingLatestData(multipleTracking, awbStatus);
    } else if (singleTracking) {
      this.trackingNumber = singleTracking;
      if (this.trackingNumber != "") {
        this.onGettingUniqueNum(this.trackingNumber);
        this.getShippingLatestData(this.trackingNumber, awbStatus);
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

  onBack() {
    this.trackingMode = false;
    this.trackingNumber = null;
  }

  ngOnDestroy(): void {
    onScan.detachFrom(document);
  }
}
