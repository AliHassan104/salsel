import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ButtonModule } from 'primeng/button';
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
import { PaginatorModule } from 'primeng/paginator';
import { ProgressBarModule } from 'primeng/progressbar';
import { RadioButtonModule } from 'primeng/radiobutton';
import { RatingModule } from 'primeng/rating';
import { RippleModule } from 'primeng/ripple';
import { SliderModule } from 'primeng/slider';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { TooltipModule } from 'primeng/tooltip';
import { TicketsFormRoutingModule } from '../../Tickets/update/tickets-form-routing.module';
import { BookingformComponent } from './bookingform.component';
import { BookingFormRoutingModule } from './booking-form-routing.module';



@NgModule({
  declarations: [BookingformComponent],
  imports: [
    CommonModule,
    TableModule,
    ReactiveFormsModule,
    RatingModule,
    ButtonModule,
    SliderModule,
    InputTextModule,
    ToggleButtonModule,
    RippleModule,
    MultiSelectModule,
    ProgressBarModule,
    ToastModule,
    DialogModule,
    FormsModule,
    AutoCompleteModule,
    CalendarModule,
    ChipsModule,
    DropdownModule,
    InputMaskModule,
    InputNumberModule,
    CascadeSelectModule,
    InputTextareaModule,
    RadioButtonModule,
    PaginatorModule,
    TooltipModule,
    BookingFormRoutingModule,
  ],
})
export class BookingFormModule {}
