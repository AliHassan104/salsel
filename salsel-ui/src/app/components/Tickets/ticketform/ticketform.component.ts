import { Component, OnInit, OnDestroy } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { TicktingService } from "src/app/components/Tickets/tickting.service";
import { HttpClient } from "@angular/common/http";
import { Ticket } from "src/app/api/ticket";
import { MessageService } from "primeng/api";

@Component({
  selector: "app-ticketform",
  templateUrl: "./ticketform.component.html",
  styleUrls: ["./ticketform.component.scss"],
  providers: [MessageService],
})
export class TicketformComponent implements OnInit, OnDestroy {
  editMode;
  editId;
  singleTicket;
  constructor(
    private _ticketService: TicktingService,
    private router: Router,
    private http: HttpClient,
    private messageService: MessageService
  ) {
    this._ticketService.editTicketMode.subscribe((res) => {
      this.editMode = res;
    });

    this._ticketService.editId.subscribe((res) => {
      this.editId = res;
    });
  }

  ticketFlag = [true, false];
  ticketForm!: FormGroup;
  selectedCurrency: string;
  currencies = ["SAR", "AED", "BHD", "KWD", "OMR", "SDG", "CNY", "USD"];

  cities = [
    "New York",
    "Los Angeles",
    "Chicago",
    "Houston",
    "London",
    "Manchester",
    "Birmingham",
    "Glasgow",
    "Toronto",
    "Vancouver",
    "Montreal",
    "Calgary",
    "Sydney",
    "Melbourne",
    "Brisbane",
    "Perth",
    "Berlin",
    "Hamburg",
    "Munich",
    "Cologne",
  ];

  countries = [
    "Australia",
    "Brazil",
    "China",
    "Egypt",
    "France",
    "Germany",
    "India",
    "Japan",
    "Spain",
    "United States",
  ];

  productType = [
    "Electronics",
    "Clothing",
    "Home and Kitchen",
    "Books",
    "Sports and Outdoors",
  ];

  dutyTaxes = ["Bill Shipper", "Bill Consignee"];

  serviceType = [
    "Standard Shipping",
    "Express Shipping",
    "Same-Day Delivery",
    "International Shipping",
    "Free Shipping",
  ];

  ngOnInit(): void {
    console.log(this.editMode);

    // Reactive Form
    this.ticketForm = new FormGroup({
      shipperName: new FormControl(null),
      shipperContactNumber: new FormControl(null),
      pickupAddress: new FormControl(null),
      shipperRefNumber: new FormControl(null),
      originCountry: new FormControl(null),
      originCity: new FormControl(null),
      recipientsName: new FormControl(null),
      recipientsContactNumber: new FormControl(null),
      destinationCountry: new FormControl(null),
      destinationCity: new FormControl(null),
      deliveryAddress: new FormControl(null),
      pickupDate: new FormControl(null),
      pickupTime: new FormControl(null),
      department: new FormControl(null, Validators.required),
      departmentCategory: new FormControl(null, Validators.required),
      assignedTo: new FormControl(null, Validators.required),
      category: new FormControl(null, Validators.required),
      createdAt: new FormControl(null),
      createdBy: new FormControl(null),
      status: new FormControl(null, Validators.required),
      ticketFlag: new FormControl(null, Validators.required),
    });

    if (this.editId === "") {
      console.log("hello");
      console.log(this.editId);
    } else {
      this._ticketService.getSingleTicket(this.editId).subscribe((res) => {
        this.singleTicket = res;
        this.ticketForm.setValue({
          shipperName: this.singleTicket.shipperName,
          shipperContactNumber: this.singleTicket.shipperContactNumber,
          pickupAddress: this.singleTicket.pickupAddress,
          shipperRefNumber: this.singleTicket.shipperRefNumber,
          originCountry: this.singleTicket.originCountry,
          originCity: this.singleTicket.originCity,
          recipientsName: this.singleTicket.recipientsName,
          recipientsContactNumber: this.singleTicket.recipientsContactNumber,
          destinationCountry: this.singleTicket.destinationCountry,
          destinationCity: this.singleTicket.destinationCity,
          deliveryAddress: this.singleTicket.deliveryAddress,
          pickupDate: this.singleTicket.pickupDate,
          pickupTime: this.singleTicket.pickupTime,
          department: this.singleTicket.department,
          departmentCategory: this.singleTicket.departmentCategory,
          assignedTo: this.singleTicket.assignedTo,
          category: this.singleTicket.category,
          createdAt: this.singleTicket.createdAt,
          createdBy: this.singleTicket.createdBy,
          status: this.singleTicket.status,
          ticketFlag: this.singleTicket.ticketFlag,
        });
      });
    }
  }

  onSubmit(data: Ticket) {
    if (this.editMode) {
      this.router.navigate(["tickets"]);
      this.http
        .put<any>(`http://localhost:8080/api/ticket/${this.editId}`, data)
        .subscribe(() => {
          this.router.navigate(["tickets"]);
        });
    } else {
      // Create Ticket
      this._ticketService.createTicket(data).subscribe();
      this.router.navigate(["tickets"]);
      this.ticketForm.reset();
      this.show();
      console.log(this.ticketForm.value);
    }
  }

  onCancel() {
    this.router.navigate(["tickets"]);
  }

  //   Pop up message
  show() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket added succesfully",
    });
  }

  ngOnDestroy(): void {
    this._ticketService.editTicketMode.next(false);
    this._ticketService.editId.next("");
  }
}
