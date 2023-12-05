import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { ServiceTypeListComponent } from './list/service-type-list.component';
import { ServiceTypeComponent } from './update/service-type.component';

// const routes: Routes = [];

const routes:Route[] = [
  {path: '', component: ServiceTypeComponent},
  {path: ':/id', component: ServiceTypeComponent},
  {path: 'list', component: ServiceTypeListComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ServiceTypeRoutingModule { }
