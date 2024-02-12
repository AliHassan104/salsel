import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketCategoryFormComponent } from './ticket-category-form.component';
import { Route, RouterModule } from '@angular/router';

const routes: Route[] = [{ path: "", component: TicketCategoryFormComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TicketCategoryFormRoutingModule {}
