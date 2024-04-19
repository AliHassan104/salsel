import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HrModuleFormComponent } from './hr-module-form.component';
import { Route, RouterModule } from '@angular/router';


const routes: Route[] = [
  {
    path: "",
    component: HrModuleFormComponent,
  },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HrFormRoutingModule {}
