import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { IAddressBook } from '../../addressBook/model/addressBookDto';
import { WebRatesService } from '../web-rates.service';
import { IWebRate } from '../model/webRateDto';

@Component({
  selector: "app-web-rate-view",
  templateUrl: "./web-rate-view.component.html",
  styleUrls: ["./web-rate-view.component.scss"],
})
export class WebRateViewComponent {
  singleAddress?: IWebRate;
  id: any;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private addressBookService: WebRatesService,
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
