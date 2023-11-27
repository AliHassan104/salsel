import { HttpClient } from "@angular/common/http";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { TicktingService } from "src/app/components/Tickets/tickting.service";

@Component({
  selector: "app-awbcreation",
  templateUrl: "./awbcreation.component.html",
  styleUrls: ["./awbcreation.component.scss"],
})
export class AwbcreationComponent implements OnInit, OnDestroy {
  editMode;
  editId;
  sTicket;
  constructor(
    private _ticketService: TicktingService,
    private router: Router,
    private http: HttpClient
  ) {
    this._ticketService.editTicketMode.subscribe((res) => {
      this.editMode = res;
    });

    this._ticketService.editId.subscribe((res) => {
      this.editId = res;
    });
  }
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
      shipperDetails: new FormGroup({
        shipperName: new FormControl(null),
        shipperContact: new FormControl(null),
        pickUpAddress: new FormControl(null),
        shipperRef: new FormControl(null),
        originCountry: new FormControl(null),
        originCity: new FormControl(null),
      }),
      recipientDetails: new FormGroup({
        recipientName: new FormControl(null),
        recipientContact: new FormControl(null),
        destinationCountry: new FormControl(null),
        destinationCity: new FormControl(null),
        deliveryAddress: new FormControl(null),
      }),
      pickupDateTime: new FormControl(null, Validators.required),
      shipmentDetails: new FormGroup({
        productType: new FormControl(null, Validators.required),
        serviceType: new FormControl(null, Validators.required),
        content: new FormControl(null, Validators.required),
        weight: new FormControl(null, Validators.required),
        pieces: new FormControl(null, Validators.required),
        amountCurrency: new FormGroup({
          currency: new FormControl(null, Validators.required),
          amount: new FormControl(null, Validators.required),
        }),
        dutyTaxBillingTo: new FormControl(null, Validators.required),
      }),
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

  onSubmit(data: any) {
    if (this.editMode) {
      this.router.navigate(["tickets"]);
      this.http
        .put<any>(`http://localhost:5000/api/v1/notes/${this.editId}`, data)
        .subscribe(() => {
          this.router.navigate(["tickets"]);
        });
    } else {
      // Create Ticket
      this._ticketService.createTicket(data).subscribe();
      console.log(data);
      this.ticketForm.reset();
    }
  }

  onCancel() {
    this.router.navigate(["tickets"]);
  }

  ngOnDestroy(): void {
    this._ticketService.editTicketMode.next(false);
    this._ticketService.editId.next("");
  }
}
