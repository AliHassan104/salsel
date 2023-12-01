import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, NavigationEnd, Router } from "@angular/router";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";
import { HttpClient } from "@angular/common/http";
import { Ticket } from "src/app/api/ticket";
import { MessageService } from "primeng/api";
import { environment } from "src/environments/environment";
import { DropdownService } from "src/app/service/dropdown.service";
import { FormvalidationService } from "../service/formvalidation.service";

@Component({
  selector: "app-ticketform",
  templateUrl: "./ticketform.component.html",
  styleUrls: ["./ticketform.component.scss"],
  providers: [MessageService],
})
export class TicketformComponent implements OnInit {
  // ALL PRODUCT FIELDS
  productFields;

  // DROPDOWN VALUES FROM PRODUCT FIELD
  status;
  ticketFlag;
  categories;
  assignedTo;

  // FOR EDIT PURPOSE
  editMode;
  editId;
  singleTicket;

  //   FORM GROUP TICKET FORM
  ticketForm!: FormGroup;

  //   CONSTRUCTOR
  constructor(
    private _ticketService: TicktingService,
    private router: Router,
    private http: HttpClient,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private dropdownService: DropdownService,
    private formService: FormvalidationService
  ) {}

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

  serviceType = [
    "Standard Shipping",
    "Express Shipping",
    "Same-Day Delivery",
    "International Shipping",
    "Free Shipping",
  ];

  ngOnInit(): void {
    // TICKET FORM CONTROLS

    this.ticketForm = new FormGroup({
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
      department: new FormControl(null, Validators.required),
      departmentCategory: new FormControl(null, Validators.required),
      assignedTo: new FormControl(null, Validators.required),
      category: new FormControl(null, Validators.required),
      createdAt: new FormControl(null, Validators.required),
      status: new FormControl(null, Validators.required),
      ticketFlag: new FormControl(null, Validators.required),
    });

    // QUERY PARAMS FOR EDIT PURPOSE
    this.route.queryParams.subscribe((params) => {
      // Retrieve editMode and id from the query parameters
      if (params["id"] != null) {
        this.editMode = params["updateMode"] === "true"; // Convert to boolean
        this.editId = +params["id"]; // Convert to number
      } else {
        this.editMode = false;
      }
    });

    // ON FORM EDIT
    if (this.editId != null) {
      this._ticketService.getSingleTicket(this.editId).subscribe((res) => {
        this.singleTicket = res;
        let pickDate = new Date(this.singleTicket.pickupDate);
        let cretedAt = new Date(this.singleTicket.createdAt);
        let pickTimeArray = this.singleTicket.pickupTime;
        let pickTime = new Date(
          `2023-11-12 ${pickTimeArray[0]}:${pickTimeArray[1]}:${pickTimeArray[2]}`
        );

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
          pickupDate: pickDate,
          pickupTime: pickTime,
          department: this.singleTicket.department,
          departmentCategory: this.singleTicket.departmentCategory,
          assignedTo: this.singleTicket.assignedTo,
          category: this.singleTicket.category,
          createdAt: cretedAt,
          status: this.singleTicket.status,
          ticketFlag: this.singleTicket.ticketFlag,
        });
      });
    }

    // GET ALL DROPDOWNS FROM PRODUCT FIELD UPON PAGE RELOAD

    this.getAllProductFields();
  }

  //   GET ALL PRODUCT FIELD

  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productFields = res;
      this.ticketFlag = this.productFields[1].productFieldValuesList;
      this.status = this.productFields[2].productFieldValuesList;
      this.categories = this.productFields[0].productFieldValuesList;
      this.assignedTo = this.productFields[5].productFieldValuesList;
    });
  }

  //  ON TICKET FORM SUBMIT

  onSubmit() {
    if (this.ticketForm.valid) {
      // Date and Time get from form
      let ticketDate: Date = this.ticketForm.value.pickupDate;
      let ticketT: Date = this.ticketForm.value.pickupTime;
      let createdAt: Date = this.ticketForm.value.createdAt;
      // Now converting date in to our desired format
      let formattedDate: string = this._ticketService.formatDate(ticketDate);

      // Now converting date in to our desired format
      let formattedCreatedAt: string =
        this._ticketService.formatCreatedAt(createdAt);

      // Now converting Time in to our desired format
      const ticketTime: string =
        this._ticketService.formatDateToHHMMSS(ticketT);

      //   To save time
      const formValue = this.ticketForm.value;
      const ticketData: Ticket = {
        assignedTo: formValue.assignedTo,
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
        category: formValue.category,
        createdAt: formattedCreatedAt,
        status: formValue.status,
        ticketFlag: formValue.ticketFlag,
        department: formValue.department,
        departmentCategory: formValue.departmentCategory,
      };

      if (this.editMode) {
        this.http
          .put<any>(`${environment.URL}ticket/${this.editId}`, ticketData)
          .subscribe(() => {
            this.update();
            this.router.navigate(["tickets"]);
          });
      } else {
        //   Create Ticket
        this._ticketService.createTicket(ticketData).subscribe();
        this.success();
        this.router.navigate(["tickets"]);
        this.ticketForm.reset();
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.ticketForm);
    }
  }

  // PART OF POPUP

  onCancel() {
    this.router.navigate(["tickets"]);
  }

  //   Pop up message
  success() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket added succesfully",
    });
  }

  update() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket Updated succesfully",
    });
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }
}
