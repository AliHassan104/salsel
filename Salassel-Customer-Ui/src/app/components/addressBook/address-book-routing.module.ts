import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Route, RouterModule } from '@angular/router';
import { AddressBookListComponent } from './list/address-book-list.component';
import { AddressBookDetailsComponent } from './view/address-book-details.component';

const routes: Route[] = [
  { path: "", component: AddressBookListComponent },
  { path: ":id", component: AddressBookDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AddressBookRoutingModule {}
