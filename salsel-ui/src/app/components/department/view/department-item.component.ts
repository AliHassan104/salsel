import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-department-item',
  templateUrl: './department-item.component.html',
  styleUrls: ['./department-item.component.scss']
})
export class DepartmentItemComponent {

  constructor(
    private activatedRoute: ActivatedRoute,
    // private _ticketService: TicktingService
  ) {}
  display: any;
  sTicket: any;
  singleTicket;

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      this.onView(a);
    });
  }

  //   On Single Ticket View
  onView(id) {
    this.display = true;
    // this._ticketService.getSingleTicket(id).subscribe((res) => {
    //   this.sTicket = res;
    //   this.singleTicket = this.sTicket.note;
    //   console.log(this.singleTicket);
    // });
  }
}
