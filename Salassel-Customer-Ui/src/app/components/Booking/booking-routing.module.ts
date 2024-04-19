import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookinglistComponent } from './list/bookinglist.component';
import { BookingitemComponent } from './view/bookingitem.component';
import { Route, RouterModule } from '@angular/router';


const routes: Route[] = [
  { path: "", component: BookinglistComponent },
  { path: ":id", component: BookingitemComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class BookingRoutingModule {}
