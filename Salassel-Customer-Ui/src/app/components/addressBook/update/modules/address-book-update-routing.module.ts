import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddressBookComponent } from '../address-book.component';
import { Route, RouterModule } from '@angular/router';

const routes: Route[] = [{ path: "", component: AddressBookComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AddressBookUpdateRoutingModule {}
