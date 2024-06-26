import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from "@angular/core";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { map, Observable, switchMap } from "rxjs";
import { AddressBookService } from "src/app/components/addressBook/service/address-book.service";
import { SessionStorageService } from "src/app/components/auth/service/session-storage.service";
import { AirbillService } from "src/app/components/awb/service/airbill.service";
import { BillingService } from "src/app/components/billing/service/billing.service";
import { CountryService } from "src/app/components/country/service/country.service";
import { DropdownService } from "src/app/layout/service/dropdown.service";

declare var onScan: any;

@Component({
  selector: "app-multiple-scan",
  templateUrl: "./multiple-scan.component.html",
  styleUrls: ["./multiple-scan.component.scss"],
  providers: [MessageService],
})
export class MultipleScanComponent implements OnInit, OnDestroy {
  productField?;
  status?;
  selectedStatus;

  removedAwb: any;

  removeDialog: any;
  airBills = [];
  uniqueScanNum;
  removeId: any;
  scanOptions;
  trackingNumber;
  updatedStatuses: any = {};
  trackingNumbers = [];

  constructor(
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService,
    private awbService: AirbillService,
    private billingService: BillingService
  ) {
    onScan.attachTo(document, {
      onScan: (sScanned, iQty) => {
        this.uniqueScanNum = sScanned;
        this.beep();
        this.getAwbOnScan(sScanned);
      },
    });
  }
  loading: any;
  @ViewChild("trackingField") trackingField: ElementRef;
  @ViewChild("beepSound") beepSound: ElementRef<HTMLAudioElement>;

  ngOnInit(): void {
    this.getAllProductFields();
  }

  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productField = res;

      this.scanOptions = this.dropdownService.extractNames(
        this.productField.filter((data) => data?.name == "Awb Status")[0]
          .productFieldValuesList
      );
    });
  }

  onStatusChange(updatedStatus: any, uniqueNumber: any) {
    if (this.updatedStatuses.hasOwnProperty(uniqueNumber)) {
      // Property exists, so update it
      this.updatedStatuses[uniqueNumber] = updatedStatus;
    } else {
      // Property does not exist, so add it
      this.updatedStatuses[uniqueNumber] = updatedStatus;
    }
  }

  onAllstatusChange(selectedStatus: any) {
    // Set the selected status for the entire table
    this.selectedStatus = selectedStatus;

    // Iterate through each item in airBills and trigger onStatusChange for each item
    this.airBills.forEach((awb: any) => {
      this.onStatusChange(selectedStatus, awb.uniqueNumber);
    });
  }

  onUpdateStatus() {
    const allKeysNotEmpty = Object.keys(this.updatedStatuses).every(
      (key) => !!this.updatedStatuses[key]
    );

    if (
      this.updatedStatuses != null &&
      Object.keys(this.updatedStatuses).length > 0 &&
      allKeysNotEmpty
    ) {
      this.awbService
        .updateMultipleAwbTrackingStatus(this.updatedStatuses)
        .pipe(
          switchMap((res: any) => {
            return this.downloadScanReport(res);
          })
        )
        .subscribe(
          (res: any) => {
            this.messageService.add({
              severity: "success",
              summary: "Sucess",
              detail: "All Airbills Status Updated",
            });
            this.airBills = [];
            this.selectedStatus = "";
            this.updatedStatuses = {};
          },
          (error) => {
            this.messageService.add({
              severity: "error",
              summary: "Error",
              detail: error?.error?.error,
            });
          }
        );
    } else {
      this.messageService.add({
        severity: "warn",
        summary: "Warning",
        detail: "No Change Detect.",
      });
    }
  }

  downloadScanReport(data: any): Observable<any> {
    return this.awbService.getMultipleAwbScanReport(data).pipe(
      map((Res: any) => {
        this.awbService.downloadExcelFile(Res, "MultipleScanReport.xlsx");
      })
    );
  }

  beep() {
    this.beepSound?.nativeElement?.play();
  }

  getAwbOnScan(uniqueNumber: any) {
    const existingItem = this.airBills.find(
      (item) => item?.uniqueNumber === parseInt(uniqueNumber)
    );

    if (!existingItem) {
      this.awbService.getSingleBillByUniqueNumber(uniqueNumber).subscribe(
        (res: any) => {
          this.airBills.push(res);
          if (this.airBills.length > 1) {
            this.airBills.forEach((awb: any) => {
              this.onStatusChange(this.selectedStatus, awb.uniqueNumber);
            });
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
    } else {
      // If the tracking number already exists, show a message
      this.messageService.add({
        severity: "warn",
        summary: "Warning",
        detail: "Airbill with that Tracking Number already exists",
      });
    }
  }

  onTrackTrackingNumber(inputValue: string): void {
    if (inputValue) {
      const values = inputValue
        .split(/[\n\s,]+/)
        .map((value) => value.trim())
        .filter((value) => value)
        .map((value) => {
          const num = Number(value);
          return isNaN(num) ? null : num;
        })
        .filter((value) => value !== null);

      this.trackingNumbers = values;

      this.awbService.getBillTrackingNumbers(this.trackingNumbers).subscribe(
        (res: any[]) => {
          if (res != null && res.length > 0) {
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
              severity: "warn",
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
    }else{
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: "Please insert a tracking number",
        });
    }
  }

  confirmRemoveSelected() {
    this.airBills.splice(this.removeId, 1);

    // If the item is also present in updatedStatuses, remove it from there as well
    if (this.updatedStatuses.hasOwnProperty(this.removedAwb.uniqueNumber)) {
      delete this.updatedStatuses[this.removedAwb.uniqueNumber];
    }
    this.removeDialog = false;
  }

  removeAwb(awb: any) {
    const index = this.airBills.indexOf(awb);

    if (index !== -1) {
      this.removedAwb = awb;
      this.removeId = index;
      this.removeDialog = true;
    }
  }

  removeAll() {
    this.airBills = [];
    this.updatedStatuses = {};
    this.selectedStatus = "";
  }

  success() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Activation Successfull",
    });
  }

  alert() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Deactivation Successfull",
    });
  }

  ngOnDestroy(): void {
    onScan.detachFrom(document);
  }
}
