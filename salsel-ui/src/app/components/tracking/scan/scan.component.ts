import { Component } from '@angular/core';

@Component({
  selector: "app-scan",
  templateUrl: "./scan.component.html",
  styleUrls: ["./scan.component.scss"],
})
export class ScanComponent {
  singleOrMultipleMode: boolean = true;
  singleScan: boolean = false;
  multipleScan: boolean = false;

  onSingleScan() {
    this.singleScan = true;
    this.singleOrMultipleMode = false;
  }

  onMultipleScan(){
    this.multipleScan = true;
    this.singleOrMultipleMode = false;
  }

  onBack() {
    this.multipleScan = false;
    this.singleScan = false;
    this.singleOrMultipleMode = true;
  }
}
