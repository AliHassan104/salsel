import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BillingComponent } from './list/billing.component';
import { BillingViewComponent } from './view/billing-view.component';
import { Route, RouterModule } from '@angular/router';


const routes: Route[] = [
  { path: "", component: BillingComponent },
  { path: ":id", component: BillingViewComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class BillingRoutingModule {}
