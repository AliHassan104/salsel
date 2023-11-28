import { HttpClient } from "@angular/common/http";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AirbillService } from "../airbill.service";
import { MessageService } from "primeng/api";
import { Airbill } from "src/app/api/airbill";
import { TicktingService } from "../../Tickets/tickting.service";

@Component({
  selector: "app-awbcreation",
  templateUrl: "./awbcreation.component.html",
  styleUrls: ["./awbcreation.component.scss"],
  providers: [MessageService],
})
export class AwbcreationComponent implements OnInit, OnDestroy {
  TicketMode;
  TicketId;
  editMode;
  editId;
  singleBill;
  singleTicket;
  constructor(
    private _airbillService: AirbillService,
    private _ticketService: TicktingService,
    private router: Router,
    private http: HttpClient,
    private MessageService: MessageService
  ) {
    // EDIT MODE
    this._airbillService.editBillMode.subscribe((res) => {
      this.editMode = res;
    });
    // EDIT ID
    this._airbillService.editBillId.subscribe((res) => {
      this.editId = res;
    });
    // CREATE FROM TICKET ID
    this._airbillService.createBillThrTickId.subscribe((res) => {
      this.TicketId = res;
    });
    // CREATE FROM TICKET MODE
    this._airbillService.createBillThrTicMode.subscribe((res) => {
      this.TicketMode = res;
    });
  }
  awbForm!: FormGroup;
  selectedCurrency: string;
  currencies = ["SAR", "AED", "BHD", "KWD", "OMR", "SDG", "CNY", "USD"];

  status = [true, false];

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
    //Setting Up Reactive Form
    this.awbForm = new FormGroup({
      shipperName: new FormControl(null, Validators.required),
      shipperContactNumber: new FormControl(null, Validators.required),
      pickupAddress: new FormControl(null, Validators.required),
      shipperRefNumber: new FormControl(null, Validators.required),
      originCountry: new FormControl(null, Validators.required),
      originCity: new FormControl(null, Validators.required),
      recipientsName: new FormControl(null, Validators.required),
      recipientsContactNumber: new FormControl(null, Validators.required),
      destinationCountry: new FormControl(null, Validators.required),
      destinationCity: new FormControl(null, Validators.required),
      deliveryAddress: new FormControl(null, Validators.required),
      pickupDate: new FormControl("", Validators.required),
      pickupTime: new FormControl("", Validators.required),
      productType: new FormControl(null, Validators.required),
      serviceType: new FormControl(null, Validators.required),
      content: new FormControl(null, Validators.required),
      weight: new FormControl(null, Validators.required),
      pieces: new FormControl(null, Validators.required),
      currency: new FormControl(null, Validators.required),
      amount: new FormControl(null, Validators.required),
      dutyAndTaxesBillTo: new FormControl(null, Validators.required),
      status: new FormControl(null, Validators.required),
    });

    // FOR EDIT BILL
    if (this.editId !== "") {
      this._airbillService.getSingleBill(this.editId).subscribe((res) => {
        this.singleBill = res;
        let pickDate = new Date(this.singleBill.pickupDate);
        let pickTimeArray = this.singleBill.pickupTime;
        let pickTime = new Date(`2023-11-12 ${pickTimeArray}`);

        this.awbForm.setValue({
          shipperName: this.singleBill.shipperName,
          shipperContactNumber: this.singleBill.shipperContactNumber,
          pickupAddress: this.singleBill.pickupAddress,
          shipperRefNumber: this.singleBill.shipperRefNumber,
          originCountry: this.singleBill.originCountry,
          originCity: this.singleBill.originCity,
          recipientsName: this.singleBill.recipientsName,
          recipientsContactNumber: this.singleBill.recipientsContactNumber,
          destinationCountry: this.singleBill.destinationCountry,
          destinationCity: this.singleBill.destinationCity,
          deliveryAddress: this.singleBill.deliveryAddress,
          pickupDate: pickDate,
          pickupTime: pickTime,
          status: this.singleBill.status,
          dutyAndTaxesBillTo: this.singleBill.dutyAndTaxesBillTo,
          weight: this.singleBill.weight,
          amount: this.singleBill.amount,
          content: this.singleBill.content,
          currency: this.singleBill.currency,
          pieces: this.singleBill.pieces,
          serviceType: this.singleBill.serviceType,
          productType: this.singleBill.productType,
        });
      });
    }

    // TO CREATE BILL FROM TICKET VIEW PAGE
    if (this.TicketId !== "") {
      this._ticketService.getSingleTicket(this.TicketId).subscribe((res) => {
        this.singleTicket = res;
        let pickDate = new Date(this.singleTicket.pickupDate);
        let pickTimeArray = this.singleTicket.pickupTime;
        let pickTime = new Date(
          `2023-11-12 ${pickTimeArray[0]}:${pickTimeArray[1]}:${pickTimeArray[2]}`
        );
        this.awbForm.patchValue({
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
          pickupDate: pickDate,
          pickupTime: pickTime,
        });
      });
    }
  }

  // Pop up message
  success() {
    this.MessageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket added succesfully",
    });
  }

  update() {
    this.MessageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket Updated succesfully",
    });
  }

  alert() {
    this.MessageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }

  onSubmit() {
    if (this.awbForm.valid) {
      // Date and Time get from form
      let ticketDate: Date = this.awbForm.value.pickupDate;
      let ticketT: Date = this.awbForm.value.pickupTime;
      // Now converting date in to our desired format
      let formattedDate: string = this._airbillService.formatDate(ticketDate);

      // Now converting Time in to our desired format
      const ticketTime: string =
        this._airbillService.formatDateToHHMMSS(ticketT);

      //   To save time
      const formValue = this.awbForm.value;
      const billData: Airbill = {
        shipperName: formValue.shipperName,
        shipperContactNumber: formValue.shipperContactNumber,
        pickupAddress: formValue.pickupAddress,
        shipperRefNumber: formValue.shipperRefNumber,
        originCountry: formValue.originCountry,
        originCity: formValue.originCity,
        recipientsName: formValue.recipientsName,
        recipientsContactNumber: formValue.recipientsContactNumber,
        destinationCountry: formValue.destinationCountry,
        destinationCity: formValue.destinationCity,
        deliveryAddress: formValue.deliveryAddress,
        pickupDate: formattedDate,
        pickupTime: ticketTime,
        status: formValue.status,
        dutyAndTaxesBillTo: formValue.dutyAndTaxesBillTo,
        weight: formValue.weight,
        amount: formValue.amount,
        content: formValue.content,
        currency: formValue.currency,
        pieces: formValue.pieces,
        serviceType: formValue.serviceType,
        productType: formValue.productType,
      };

      if (this.editMode) {
        this.http
          .put<any>(`http://localhost:8080/api/awb/${this.editId}`, billData)
          .subscribe(() => {
            this.update();
            this.router.navigate(["airwaybills"]);
          });
      } else {
        //   Create Ticket
        this._airbillService.createBill(billData).subscribe((res) => {
          this.success();
          this.router.navigate(["airwaybills"]);
          this.awbForm.reset();
        });
      }
    } else {
      this.alert();
    }
  }

  onCancel() {
    this.router.navigate(["airwaybills"]);
  }

  //   Pop up message

  ngOnDestroy(): void {
    this._airbillService.editBillMode.next(false);
    this._airbillService.editBillId.next("");
    this._airbillService.createBillThrTicMode.next(false);
    this._airbillService.createBillThrTickId.next("");
  }
}
