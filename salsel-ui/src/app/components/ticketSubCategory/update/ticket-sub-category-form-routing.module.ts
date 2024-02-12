import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketSubCategoryComponent } from './ticket-sub-category.component';
import { Route, RouterModule } from '@angular/router';

const routes: Route[] = [{ path: "", component: TicketSubCategoryComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TicketSubCategoryFormRoutingModule {}
