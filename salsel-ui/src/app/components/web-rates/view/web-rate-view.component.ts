import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AddressBookService } from '../../addressBook/service/address-book.service';
import { IAddressBook } from '../../addressBook/model/addressBookDto';

@Component({
  selector: "app-web-rate-view",
  templateUrl: "./web-rate-view.component.html",
  styleUrls: ["./web-rate-view.component.scss"],
})
export class WebRateViewComponent {
  singleAddress?: IAddressBook;
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
