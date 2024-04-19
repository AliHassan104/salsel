import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HrModuleDataComponent } from './list/hr-module-data.component';
import { HrModuleViewComponent } from './view/hr-module-view.component';
import { Route, RouterModule } from '@angular/router';


const routes: Route[] = [
  { path: "", component: HrModuleDataComponent },
  { path: ":id", component: HrModuleViewComponent },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HrModuleRoutingModule {}
