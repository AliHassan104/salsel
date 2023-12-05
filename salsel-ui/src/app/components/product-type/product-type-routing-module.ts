import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { ProductTypeListComponent } from './list/product-type-list.component';
import { ProductTypeComponent } from './update/product-type.component';

// const routes: Routes = [];

const routes:Route[] = [
  {path: '', component: ProductTypeComponent},
  {path: ':/id', component: ProductTypeComponent},
  {path: 'list', component: ProductTypeListComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductTypeRoutingModule { }
