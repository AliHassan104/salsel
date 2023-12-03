import { HttpClient } from "@angular/common/http";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { AirbillService } from "../service/airbill.service";
import { MessageService } from "primeng/api";
import { Airbill } from "src/app/api/airbill";
import { TicktingService } from "../../Tickets/service/tickting.service";
import { environment } from "src/environments/environment";
import { DropdownService } from "src/app/service/dropdown.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";

@Component({
  selector: "app-awbcreation",
  templateUrl: "./awbcreation.component.html",
  styleUrls: ["./awbcreation.component.scss"],
  providers: [MessageService],
})
export class AwbcreationComponent implements OnInit, OnDestroy {
  //   ALL PRODUCT FIELD FOR DROPDOWNS
  productFields?;

  // DROPDOWNS FORM PRODUCT FIELD
  currencies?;
  status?;
  dutyTaxes?;
  requestTypes?;
  countries;
  originCities?;
  destinationCities?;
  productType?;
  serviceType?;

  // FOR EDIT AND CREATE AIRBILL FROM TICKET
  ticketMode;
  TicketId;
  editMode;
  editId;
  createAWB;
  updateAWB;
  productTypeId;
  serviceTypeId;

  // SINGLE BILL AND TICKET STORE IN IT
  singleBill;
  singleTicket;

  //   CONSTRUCTOR
  constructor(
    private _airbillService: AirbillService,
    private _ticketService: TicktingService,
    private router: Router,
    private http: HttpClient,
    private MessageService: MessageService,
    private route: ActivatedRoute,
    private dropdownService: DropdownService,
    private formService: FormvalidationService
  ) {
    this._airbillService.updateAWB.subscribe((res) => {
      this.updateAWB = res;
    });
    this._airbillService.CreateAWB.subscribe((res) => {
      this.createAWB = res;
    });
  }
  awbForm!: FormGroup;

  ngOnInit(): void {
    //Setting Up Reactive Form
    this.awbForm = new FormGroup({
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
      productType: new FormControl(null, Validators.required),
      serviceType: new FormControl(null, Validators.required),
      content: new FormControl(null, Validators.required),
      weight: new FormControl(null, Validators.required),
      pieces: new FormControl(null, Validators.required),
      currency: new FormControl(null, Validators.required),
      amount: new FormControl(null, Validators.required),
      dutyAndTaxesBillTo: new FormControl(null, Validators.required),
      requestType: new FormControl(null, Validators.required),
    });

    // Query Param for edit and create from ticket
    this.route.queryParams.subscribe((params) => {
      // Retrieve editMode and id from the query parameters
      if (params["id"] != null && this.updateAWB == true) {
        this.editMode = params["updateMode"] === "true"; // Convert to boolean
        this.editId = +params["id"]; // Convert to number
      }
      //   To create AWB form ticket view page
      else if (params["id"] != null && this.createAWB == true) {
        this.ticketMode = true;
        this.TicketId = +params["id"];
      } else {
        this.ticketMode = false;
        this.editMode = false;
      }
    });

    // FOR EDIT BILL
    if (this.editMode) {
      this._airbillService.getSingleBill(this.editId).subscribe((res) => {
        this.getAllCities();
        this.getAllServiceTypes();
        this.singleBill = res;
        let pickDate = new Date(this.singleBill.pickupDate);
        let pickTimeArray = this.singleBill.pickupTime;
        let pickTime = new Date(`2023-11-12 ${pickTimeArray}`);

        // EDIT BILL VALUES SETUP

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
          dutyAndTaxesBillTo: this.singleBill.dutyAndTaxesBillTo,
          weight: this.singleBill.weight,
          amount: this.singleBill.amount,
          content: this.singleBill.content,
          currency: this.singleBill.currency,
          pieces: this.singleBill.pieces,
          serviceType: this.singleBill.serviceType,
          productType: this.singleBill.productType,
          requestType: "Pick-up",
        });
      });
    }

    // TO CREATE BILL FROM TICKET VIEW PAGE

    if (this.ticketMode) {
      this._ticketService.getSingleTicket(this.TicketId).subscribe((res) => {
        this.getAllCities();
        this.singleTicket = res;
        let pickDate = new Date(this.singleTicket.pickupDate);
        let pickTimeArray = this.singleTicket.pickupTime;
        let pickTime = new Date(`2023-11-12 ${pickTimeArray}`);
        this.awbForm.patchValue({
          shipperName: this.singleTicket.shipperName,
          shipperContactNumber: this.singleTicket.shipperContactNumber,
          pickupAddress: this.singleTicket.pickupAddress,
          shipperRefNumber: this.singleTicket.shipperRefNumber,
          originCountry: this.singleTicket.originCountry,
          originCity: this.singleTicket.originCity,
          recipientsName: this.singleTicket.recipientName,
          recipientsContactNumber: this.singleTicket.recipientContactNumber,
          destinationCountry: this.singleTicket.destinationCountry,
          destinationCity: this.singleTicket.destinationCity,
          deliveryAddress: this.singleTicket.deliveryAddress,
          pickupDate: pickDate,
          pickupTime: pickTime,
        });
      });
    }

    // GET VALUES OF DROPDOWNS
    this.getAllProductField();
  }

  // TO GET ALL PRODUCT FIELDS

  getAllProductField() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productFields = res;
      //   Currencies
      this.currencies = this.dropdownService.extractNames(
        this.productFields[3].productFieldValuesList
      );
      //   Duty Taxes
      this.dutyTaxes = this.dropdownService.extractNames(
        this.productFields[4].productFieldValuesList
      );
      //   Request Types
      this.requestTypes = this.dropdownService.extractNames(
        this.productFields[7].productFieldValuesList
      );
    });

    // Get All Countries

    this.dropdownService.getAllCountries().subscribe((res) => {
      this.countries = res;
      this.countries = this.dropdownService.extractNames(this.countries);
    });

    // GET ALL PRODUCT TYPES
    this.dropdownService.getAllProductTypes().subscribe((res) => {
      this.productType = res;
      this.productType = this.dropdownService.extractNames(this.productType);
    });
  }

  // GET PRODUCT TYPE BY SELECT FROM DROPDOWN
  getProductType(data) {
    this.dropdownService.getAllServiceTypes().subscribe((res) => {
      this.serviceType = res;
      let filterProductType = this.serviceType.filter(
        (type) => type.productType.name == data.value
      );
      this.serviceType = this.dropdownService.extractNames(filterProductType);
    });
  }

  //   GET ALL CITIES

  getAllCities() {
    this.dropdownService.getAllCities().subscribe((res) => {
      this.destinationCities = res;
      this.originCities = res;
      this.destinationCities = this.dropdownService.extractNames(
        this.destinationCities
      );
      this.originCities = this.dropdownService.extractNames(this.originCities);
    });
  }

  //   GET ALL SERVICE TYPES
  getAllServiceTypes() {
    this.dropdownService.getAllServiceTypes().subscribe((res) => {
      this.serviceType = res;
      this.serviceType = this.dropdownService.extractNames(this.serviceType);
    });
  }

  //   GET DESTINATION COUNTRY FROM DROPDOWN
  getDestinationCountry(country) {
    this.dropdownService.getAllCities().subscribe((res) => {
      this.destinationCities = res;
      let filterCities = this.destinationCities.filter(
        (city) => city.country.name == country.value
      );
      this.destinationCities = this.dropdownService.extractNames(filterCities);
    });
  }

  //   GET ORGIN COUNTRYFROM DROPDOWN
  getOriginCountry(country) {
    this.dropdownService.getAllCities().subscribe((res) => {
      this.originCities = res;
      let filterCities = this.originCities.filter(
        (city) => city.country.name == country.value
      );
      this.originCities = this.dropdownService.extractNames(filterCities);
    });
  }

  //   ON SUBMIT OF FORM

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
        requestType: formValue.requestType,
      };

      if (this.editMode) {
        this.http
          .put<any>(`${environment.URL}awb/${this.editId}`, billData)
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
      this.formService.markFormGroupTouched(this.awbForm);
    }
  }

  onCancel() {
    this.router.navigate(["airwaybills"]);
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

  ngOnDestroy(): void {
    this._airbillService.CreateAWB.next(false);
    this._airbillService.updateAWB.next(false);
  }
}
