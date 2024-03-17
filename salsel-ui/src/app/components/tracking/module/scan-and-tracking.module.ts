import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TrackingAndScanComponent } from '../tracking-and-scan/tracking-and-scan.component';
import { ScanTrackingRoutingModule } from './scan-tracking-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { CalendarModule } from 'primeng/calendar';
import { CascadeSelectModule } from 'primeng/cascadeselect';
import { ChipsModule } from 'primeng/chips';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { InputMaskModule } from 'primeng/inputmask';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { MultiSelectModule } from 'primeng/multiselect';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { TooltipModule } from 'primeng/tooltip';
import { ScanComponent } from '../scan/scan.component';
import { TrackingComponent } from '../tracking/tracking.component';
import { SingleScanComponent } from '../scan/single-scan/single-scan.component';
import { MultipleScanComponent } from '../scan/multiple-scan/multiple-scan.component';
import { DividerModule } from 'primeng/divider';
import { TimelineModule } from 'primeng/timeline';
import { CardModule } from 'primeng/card';


@NgModule({
  declarations: [
    TrackingAndScanComponent,
    ScanComponent,
    TrackingComponent,
    SingleScanComponent,
    MultipleScanComponent,
  ],
  imports: [
    CommonModule,
    TableModule,
    AutoCompleteModule,
    CalendarModule,
    CascadeSelectModule,
    ChipsModule,
    DropdownModule,
    DividerModule,
    InputMaskModule,
    InputNumberModule,
    InputTextModule,
    InputTextareaModule,
    MultiSelectModule,
    FormsModule,
    ReactiveFormsModule,
    ToastModule,
    TooltipModule,
    DialogModule,
    ScanTrackingRoutingModule,
    TimelineModule,
    CardModule
  ],
})
export class ScanAndTrackingModule {}
