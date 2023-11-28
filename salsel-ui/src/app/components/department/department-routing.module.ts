import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { DepartmentDataComponent } from './department-data/department-data.component';
import { DepartmentFormComponent } from './department-form/department-form.component';
import { DepartmentItemComponent } from './department-item/department-item.component';

// const routes: Routes = [];

const routes:Route[] = [
  {  path: '', component:DepartmentDataComponent } ,
  {  path: 'form', component:DepartmentFormComponent } , 
  {  path: 'form/:id', component:DepartmentItemComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DepartmentRoutingModule { }
