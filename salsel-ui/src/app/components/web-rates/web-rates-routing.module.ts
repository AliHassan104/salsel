import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WebRateListComponent } from './list/web-rate-list.component';
import { WebRateViewComponent } from './view/web-rate-view.component';
import { Route, RouterModule } from '@angular/router';

const routes: Route[] = [
  { path: "", component: WebRateListComponent },
  { path: ":id", component: WebRateViewComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class WebRatesRoutingModule {}
