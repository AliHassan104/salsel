import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { DepartmentComponent } from './update/department.component';
import { DepartmentItemComponent } from './view/department-item.component';
import { DepartmentListComponent } from './list/department-list.component';

// const routes: Routes = [];

const routes:Route[] = [
  {  path: '', component:DepartmentListComponent } ,
  {  path: 'form', component:DepartmentComponent } , 
  {  path: 'form/:id', component:DepartmentItemComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DepartmentRoutingModule { }
