import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from "@angular/core";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { Table } from "primeng/table";
import { finalize } from "rxjs";
import { IAddressBook } from "src/app/components/addressBook/model/addressBookDto";
import { AddressBookService } from "src/app/components/addressBook/service/address-book.service";
import { SessionStorageService } from "src/app/components/auth/service/session-storage.service";
import { AirbillService } from "src/app/components/awb/service/airbill.service";
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

  removeDialog: any;
  airBills = [];
  uniqueScanNum;
  removeId: any;
  scanOptions;
  trackingNumber;
  updatedStatuses: any = {};

  constructor(
    private countryService: CountryService,
    private adddressBookService: AddressBookService,
    private router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    public sessionStorageService: SessionStorageService,
    private awbService: AirbillService
  ) {
    onScan.attachTo(document, {
      onScan: (sScanned, iQty) => {
        console.log("Scanned:", iQty + "x " + sScanned);
        this.uniqueScanNum = sScanned;
        this.getAwbOnScan(sScanned);
      },
    });
  }
  loading: any;
  @ViewChild("trackingField") trackingField: ElementRef;

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
    console.log(this.updatedStatuses);
  }

  onUpdateStatus() {
    if (
      this.updatedStatuses != null &&
      Object.keys(this.updatedStatuses).length > 0
    ) {
      this.awbService
        .updateMultipleAwbTrackingStatus(this.updatedStatuses)
        .subscribe(
          (res: any) => {
            this.messageService.add({
              severity: "success",
              summary: "Sucess",
              detail: "All Airbills Status Updated",
            });
            this.airBills = [];
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

  getAwbOnScan(uniqueNumber: any) {
    const existingItem = this.airBills.find(
      (item) => item?.uniqueNumber === parseInt(uniqueNumber)
    );

    if (!existingItem) {
      this.awbService.getSingleBillByUniqueNumber(uniqueNumber).subscribe(
        (res: any) => {
          this.airBills.push(res);
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

  onTrackTrackingNumber() {
    if (this.trackingNumber) {
      const existingItem = this.airBills.find(
        (item) => item?.uniqueNumber === parseInt(this.trackingNumber)
      );

      console.log(existingItem);

      if (!existingItem) {
        this.awbService
          .getSingleBillByUniqueNumber(this.trackingNumber)
          .subscribe(
            (res: any) => {
              this.airBills.push(res);
              this.trackingField.nativeElement.value = "";
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
          detail: "Tracking number already exists",
        });
      }
    } else {
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Please insert a tracking number",
      });
    }
  }

  confirmRemoveSelected() {
    this.airBills.splice(this.removeId, 1);
    this.removeDialog = false;
  }

  removeAwb(awb: any) {
    const index = this.airBills.indexOf(awb);
    console.log(index);

    if (index !== -1) {
      this.removeId = index;
      this.removeDialog = true;
    }
  }

  removeAll() {
    this.airBills = [];
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
