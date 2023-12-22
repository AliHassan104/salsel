import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, NavigationEnd, Router } from "@angular/router";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";
import { HttpClient } from "@angular/common/http";
import { Ticket } from "src/app/components/Tickets/model/ticketValuesDto";
import { MessageService } from "primeng/api";
import { environment } from "src/environments/environment";
import { DropdownService } from "src/app/service/dropdown.service";
import { FormvalidationService } from "../service/formvalidation.service";
import { CountryService } from "../../country/service/country.service";
import { CityService } from "../../City/service/city.service";
import { DepartmentService } from "../../department/service/department.service";
import { DepartmentCategoryService } from "../../department-category/service/department-category.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { RolesService } from "../../permissions/service/roles.service";

@Component({
  selector: "app-ticketform",
  templateUrl: "./ticketform.component.html",
  styleUrls: ["./ticketform.component.scss"],
  providers: [MessageService],
})
export class TicketformComponent implements OnInit {
  params = { status: true };

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
  singleTicket: Ticket;

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
    private formService: FormvalidationService,
    private countryService: CountryService,
    private cityService: CityService,
    private departmentService: DepartmentService,
    private departmentCategoryService: DepartmentCategoryService,
    private roleService: RolesService
  ) {}

  ngOnInit(): void {
    // TICKET FORM CONTROLS

    this.ticketFormSetup();

    // QUERY PARAMS FOR EDIT PURPOSE
    this.queryParamSetup();

    // GET ALL DROPDOWNS FROM PRODUCT FIELD UPON PAGE RELOAD

    this.getAllProductFields();

    // ON FORM EDIT
    this.editForm();
  }

  ticketFormSetup() {
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
      ticketStatus: new FormControl(null, Validators.required),
      ticketFlag: new FormControl(null, Validators.required),
    });
  }

  queryParamSetup() {
    this.route.queryParams.subscribe((params) => {
      // Retrieve editMode and id from the query parameters
      if (params["id"] != null) {
        this.editMode = params["updateMode"] === "true"; // Convert to boolean
        this.editId = +params["id"]; // Convert to number
      } else {
        this.editMode = false;
      }
    });
  }

  editForm() {
    if (this.editId != null) {
      this._ticketService.getSingleTicket(this.editId).subscribe((res) => {
        this.singleTicket = res;
        this.getAllCitiesAndDepartmentCategories(
          this.singleTicket.destinationCountry,
          this.singleTicket.originCountry,
          this.singleTicket.department
        );

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

      //   Categories From Product Field
      this.categories = this.dropdownService.extractNames(
        this.productFields.filter((data) => data.name == "Category")[0]
          .productFieldValuesList
      );

      this.ticketFlag = this.dropdownService.extractNames(
        this.productFields.filter((data) => data.name == "Ticket Flag")[0]
          .productFieldValuesList
      );

      this.status = this.dropdownService.extractNames(
        this.productFields.filter((data) => data.name == "Ticket Status")[0]
          .productFieldValuesList
      );
    });

    // Get All Countries

    this.countryService.getAllCountries(this.params).subscribe((res) => {
      this.countries = res;
      this.countries = this.dropdownService.extractNames(this.countries);
    });

    // Get All Departments
    this.departmentService.getDepartments(this.params).subscribe((res) => {
      this.department = res.body;
      this.department = this.dropdownService.extractNames(this.department);
    });

    // Get All Roles
    this.roleService.getRoles().subscribe((res: any) => {
      this.assignedTo = res;
      this.assignedTo = this.dropdownService.extractNames(this.assignedTo);
    });
  }

  //   GET ALL CITIES,DEPARTMENT CATEGORIES
  getAllCitiesAndDepartmentCategories(
    destinationCountry: any,
    originCountry: any,
    department: any
  ) {
    this.cityService.getAllCities(this.params).subscribe((res) => {
      this.destinationCities = res;
      this.originCities = res;
      //   Destination Cities
      let filterDestinationCities = this.destinationCities.filter(
        (value) => value.country.name == destinationCountry
      );
      this.destinationCities = this.dropdownService.extractNames(
        filterDestinationCities
      );

      //   Origin Cities
      let filterOriginCities = this.originCities.filter(
        (value) => value.country.name == originCountry
      );
      this.originCities = this.dropdownService.extractNames(filterOriginCities);
    });

    // FOR DEPARTMENT CATEGORY
    this.departmentCategoryService
      .getDepartmentCategories(this.params)
      .subscribe((res) => {
        this.departmentCategory = res.body;
        let filterDepartments = this.departmentCategory.filter(
          (city) => city.department.name == department
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

  //   GET DEPARTMENT
  getDepartment(department) {
    this.departmentCategoryService
      .getDepartmentCategories(this.params)
      .subscribe((res) => {
        this.departmentCategory = res.body;
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
    this.cityService.getAllCities(this.params).subscribe((res) => {
      this.destinationCities = res;
      let filterCities = this.destinationCities.filter(
        (city) => city.country.name == country.value
      );
      this.destinationCities = this.dropdownService.extractNames(filterCities);
    });
  }

  //   GET ORGIN COUNTRYFROM DROPDOWN
  getOriginCountry(country) {
    this.cityService.getAllCities(this.params).subscribe((res) => {
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
      //   let createdAt: Date = this.ticketForm.value.createdAt;
      // Now converting date in to our desired format
      let formattedDate: string = this._ticketService.formatDate(ticketDate);

      // Now converting date in to our desired format
      //   let formattedCreatedAt: string =
      //     this._ticketService.formatCreatedAt(createdAt);

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
        ticketStatus: formValue.ticketStatus,
        ticketFlag: formValue.ticketFlag,
        department: formValue.department,
        departmentCategory: formValue.departmentCategory,
        createdBy: localStorage.getItem("loginUserName"),
      };

      if (this.editMode) {
        this.http
          .put<any>(`${environment.URL}ticket/${this.editId}`, ticketData)
          .subscribe(() => {
            this.update();
            this.router.navigate(["ticket/list"]);
          });
      } else {
        //   Create Ticket

        this._ticketService.createTicket(ticketData).subscribe();
        this.success();
        this.router.navigate(["ticket/list"]);
        this.ticketForm.reset();
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.ticketForm);
    }
  }

  // PART OF POPUP

  onCancel() {
    this.router.navigate(["ticket/list"]);
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
