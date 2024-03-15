import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "src/app/components/Tickets/service/formvalidation.service";
import { AirbillService } from "src/app/components/awb/service/airbill.service";
import { DropdownService } from "src/app/layout/service/dropdown.service";

declare var onScan: any;

@Component({
  selector: "app-single-scan",
  templateUrl: "./single-scan.component.html",
  styleUrls: ["./single-scan.component.scss"],
  providers: [MessageService],
})
export class SingleScanComponent implements OnInit, OnDestroy {
  trackingNumber;
  codeScan: boolean = false;
  uniqueScanNum;
  scanOptions;
  productField;
  scanForm!: FormGroup;
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
        this.uniqueScanNum = sScanned;
        this.onDetectionOfScan();
      },
    });
  }

  ngOnInit(): void {
    this.scanForm = new FormGroup({
      updatedStatus: new FormControl(null, [Validators.required]),
    });

    this.getAllProductFields();
  }

  onDetectionOfScan() {
    this.codeScan = true;
    this.beepSound?.nativeElement?.play();
    console.log(this.uniqueScanNum);
  }

  onTrackTrackingNumber(){}

  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productField = res;

      this.scanOptions = this.dropdownService.extractNames(
        this.productField.filter((data) => data?.name == "Awb Status")[0]
          .productFieldValuesList
      );
    });
  }

  onCloseScan() {
    this.uniqueScanNum = null;
  }

  onUpdateStatus(data: any) {
    if (this.scanForm.valid) {
      this._airbillService
        .updateAwbTrackingStatus(data?.updatedStatus, this.uniqueScanNum)
        .subscribe((res) => {
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Status Updated",
          });
          this.scanForm.reset();
          this.codeScan = false;
        });
    } else {
      this.formService.markFormGroupTouched(this.scanForm);
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Please ensure that required Field is filled out.",
      });
    }
  }

  ngOnDestroy(): void {
    onScan.detachFrom(document);
  }
}
