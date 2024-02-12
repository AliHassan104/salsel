import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketSubCategoryListComponent } from './list/ticket-sub-category-list.component';
import { Route, RouterModule } from '@angular/router';

const routes: Route[] = [{ path: "", component: TicketSubCategoryListComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TicketSubCategoryRoutingModule {}
