import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './list/product-list.component';
import { ProductComponent } from './product/product.component';

// const routes: Routes = [];

const routes:Route[] = [
  {path: '', component: ProductComponent},
  {path: ':/id', component: ProductComponent},
  {path: 'product-list', component: ProductListComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductFieldRoutingModule { }
