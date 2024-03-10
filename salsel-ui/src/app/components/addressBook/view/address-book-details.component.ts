import { Component } from '@angular/core';
import { AddressBookService } from '../service/address-book.service';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { IAddressBook } from '../model/addressBookDto';

@Component({
  selector: "app-address-book-details",
  templateUrl: "./address-book-details.component.html",
  styleUrls: ["./address-book-details.component.scss"],
  providers:[MessageService]
})
export class AddressBookDetailsComponent {
  singleAddress?:IAddressBook;
  id: any;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private addressBookService: AddressBookService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      this.id = a;
      this.getSingleAddress(a);
    });
  }

  getSingleAddress(id) {
    this.addressBookService.getAddressBookById(id).subscribe((res: any) => {   
      this.singleAddress = res?.body;
    });
  }
}
