import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { DepartmentCategoryDataComponent } from './department-category-data/department-category-data.component';
import { DepartmentCategoryFormComponent } from './department-category-form/department-category-form.component';
import { DepartmentCategoryItemComponent } from './department-category-item/department-category-item.component';

const routes:Route[] = [
  {  path: '', component:DepartmentCategoryDataComponent } ,
  {  path: 'form', component:DepartmentCategoryFormComponent } , 
  {  path: 'item', component:DepartmentCategoryItemComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DepartmentCategoryRoutingModule { }
