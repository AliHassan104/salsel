import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketCategoryListComponent } from './list/ticket-category-list.component';
import { Route, RouterModule } from '@angular/router';

const routes: Route[] = [
  { path: "", component: TicketCategoryListComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TicketCategoryRoutingModule {}
