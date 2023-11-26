import { Component, OnInit, OnDestroy } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { TicktingService } from "src/app/service/tickting.service";
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
  sTicket;
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

  ticketFlag = ["True", "False"];
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
      pickupDate: new FormControl(null, Validators.required),
      pickupTime: new FormControl(null, Validators.required),
      department: new FormControl(null, Validators.required),
      departmentCategory: new FormControl(null, Validators.required),
      assignedTo: new FormControl(null, Validators.required),
      category: new FormControl(null, Validators.required),
      createdAt: new FormControl(null, Validators.required),
      createdBy: new FormControl(null, Validators.required),
      status: new FormControl(null, Validators.required),
      ticketFlag: new FormControl(null, Validators.required),
    });

    if (this.editId === "") {
      console.log("hello");
      console.log(this.editId);
    } else {
      this._ticketService.getSingleTicket(this.editId).subscribe((res) => {
        this.sTicket = res;
        let sd = this.sTicket.note.shipperDetails;
        let rd = this.sTicket.note.recipientDetails;
        let shipmentD = this.sTicket.note.shipmentDetails;

        this.ticketForm.setValue({
          shipperDetails: {
            shipperName: sd.shipperName,
            shipperContact: sd.shipperContact,
            pickUpAddress: sd.pickUpAddress,
            shipperRef: sd.shipperRef,
            originCountry: sd.originCountry,
            originCity: sd.originCity,
          },
          recipientDetails: {
            recipientName: rd.recipientName,
            recipientContact: rd.recipientContact,
            destinationCountry: rd.destinationCountry,
            destinationCity: rd.destinationCity,
            deliveryAddress: rd.deliveryAddress,
          },
          pickupDateTime: this.sTicket.note.pickupDateTime,
          shipmentDetails: {
            productType: shipmentD.productType,
            serviceType: shipmentD.serviceType,
            content: shipmentD.content,
            weight: shipmentD.weight,
            pieces: shipmentD.pieces,
            amountCurrency: {
              currency: shipmentD.amountCurrency.currency,
              amount: shipmentD.amountCurrency.amount,
            },
            dutyTaxBillingTo: shipmentD.dutyTaxBillingTo,
          },
        });
      });
    }
  }

  onSubmit(data: Ticket) {
    if (this.editMode) {
      this.router.navigate(["tickets"]);
      this.http
        .put<any>(`http://localhost:5000/api/v1/notes/${this.editId}`, data)
        .subscribe(() => {
          this.router.navigate(["tickets"]);
        });
    } else {
      // Create Ticket
      //   this._ticketService.createTicket(data).subscribe();
      console.log(data);
      this.ticketForm.reset();
      this.show();
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
