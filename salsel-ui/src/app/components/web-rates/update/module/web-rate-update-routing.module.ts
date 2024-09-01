import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WebRateComponent } from '../web-rate.component';
import { Route, RouterModule } from '@angular/router';

const routes: Route[] = [{ path: "", component: WebRateComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class WebRateUpdateRoutingModule {}
