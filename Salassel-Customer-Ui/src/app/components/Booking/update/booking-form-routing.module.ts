import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingformComponent } from './bookingform.component';
import { Route, RouterModule } from '@angular/router';


const routes: Route[] = [
  {
    path: "",
    component: BookingformComponent,
  },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class BookingFormRoutingModule {}
