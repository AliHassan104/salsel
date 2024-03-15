import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TrackingAndScanComponent } from '../tracking-and-scan/tracking-and-scan.component';
import { Route, RouterModule } from "@angular/router";
import { ScanComponent } from '../scan/scan.component';
import { TrackingComponent } from '../tracking/tracking.component';

const routes: Route[] = [
  { path: "", component: TrackingAndScanComponent },
  { path: "scan", component: ScanComponent },
  { path: "tracking", component: TrackingComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ScanTrackingRoutingModule {}
