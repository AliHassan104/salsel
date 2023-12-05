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
  status?;
  ticketFlag?;
  categories?;
  assignedTo?;
  countries?;
  destinationCities?;
  originCities?;
  department?;
  departmentCategory?;

  // FOR EDIT PURPOSE
  editMode;
  editId;
  singleTicket;
  depart;
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

  ngOnInit(): void {
    // TICKET FORM CONTROLS

    this.ticketForm = new FormGroup({
      shipperName: new FormControl(null),
      shipperContactNumber: new FormControl(null),
      pickupAddress: new FormControl(null),
      shipperRefNumber: new FormControl(null),
      originCountry: new FormControl(null),
      originCity: new FormControl(null),
      recipientName: new FormControl(null),
      recipientContactNumber: new FormControl(null),
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
      ticketStatus: new FormControl(null, Validators.required),
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

    // GET ALL DROPDOWNS FROM PRODUCT FIELD UPON PAGE RELOAD

    this.getAllProductFields();

    // ON FORM EDIT
    if (this.editId != null) {
      this._ticketService.getSingleTicket(this.editId).subscribe((res) => {
        this.getCitiesAndDeptCatgories();

        this.singleTicket = res;
        let pickDate = new Date(this.singleTicket.pickupDate);
        let cretedAt = new Date(this.singleTicket.createdAt);
        let pickTimeArray = this.singleTicket.pickupTime;
        let pickTime = new Date(`2023-11-12 ${pickTimeArray}`);

        this.ticketForm.setValue({
          shipperName: this.singleTicket.shipperName,
          shipperContactNumber: this.singleTicket.shipperContactNumber,
          pickupAddress: this.singleTicket.pickupAddress,
          shipperRefNumber: this.singleTicket.shipperRefNumber,
          originCountry: this.singleTicket.originCountry,
          originCity: this.singleTicket.originCity,
          recipientName: this.singleTicket.recipientName,
          recipientContactNumber: this.singleTicket.recipientContactNumber,
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
          ticketStatus: this.singleTicket.ticketStatus,
          ticketFlag: this.singleTicket.ticketFlag,
        });
      });
    }
  }

  //   GET ALL PRODUCT FIELD

  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productFields = res;
      this.ticketFlag = this.dropdownService.extractNames(
        this.productFields[1].productFieldValuesList
      );

      this.status = this.dropdownService.extractNames(
        this.productFields[2].productFieldValuesList
      );
      this.categories = this.dropdownService.extractNames(
        this.productFields[0].productFieldValuesList
      );
      this.assignedTo = this.dropdownService.extractNames(
        this.productFields[6].productFieldValuesList
      );
    });

    // Get All Countries

    this.dropdownService.getAllCountries().subscribe((res) => {
      this.countries = res;
      this.countries = this.dropdownService.extractNames(this.countries);
    });

    // Get All Departments
    this.dropdownService.getAllDepartments().subscribe((res) => {
      this.department = res;
      this.department = this.dropdownService.extractNames(this.department);
    });
  }

  //   GET ALL CITIES,DEPARTMENT CATEGORIES
  getCitiesAndDeptCatgories() {
    this.dropdownService.getAllCities().subscribe((res) => {
      this.destinationCities = res;
      this.originCities = res;
      this.destinationCities = this.dropdownService.extractNames(
        this.destinationCities
      );
      this.originCities = this.dropdownService.extractNames(this.originCities);
    });

    this.dropdownService.getAllDepartmentCategories().subscribe((res) => {
      this.departmentCategory = res;
      this.departmentCategory = this.dropdownService.extractNames(
        this.departmentCategory
      );
    });
  }

  //   GET DEPARTMENT
  getDepartment(department) {
    // if (department.value == "Customer Service") {
    //   this.ticketForm.get("departmentCategory")?.disable();
    // }
    this.dropdownService.getAllDepartmentCategories().subscribe((res) => {
      this.departmentCategory = res;
      let filterDepartments = this.departmentCategory.filter(
        (city) => city.department.name == department.value
      );

      // Disable
      if (filterDepartments.length == 0) {
        this.ticketForm.get("departmentCategory")?.disable();
      } else {
        this.ticketForm.get("departmentCategory")?.enable();
      }

      this.departmentCategory =
        this.dropdownService.extractNames(filterDepartments);
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
        recipientName: formValue.recipientName,
        recipientContactNumber: formValue.recipientContactNumber,
        destinationCountry: formValue.destinationCountry,
        destinationCity: formValue.destinationCity,
        deliveryAddress: formValue.deliveryAddress,
        pickupDate: formattedDate,
        pickupTime: ticketTime,
        category: formValue.category,
        createdAt: formattedCreatedAt,
        ticketStatus: formValue.ticketStatus,
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
