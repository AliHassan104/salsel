import {
  AfterViewInit,
  Component,
  ElementRef,
  OnInit,
  ViewChild,
} from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, NavigationEnd, Router } from "@angular/router";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";
import { HttpClient } from "@angular/common/http";
import { Ticket } from "src/app/components/Tickets/model/ticketValuesDto";
import { MessageService } from "primeng/api";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { FormvalidationService } from "../service/formvalidation.service";
import { AccountService } from "../../accounts/service/account.service";
import { CountryService } from "src/app/service/country.service";
import { CityService } from "src/app/service/city.service";
import { DepartmentService } from "src/app/service/department.service";
import { DepartmentCategoryService } from "src/app/service/department-category.service";
import { RolesService } from "src/app/service/roles.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { Dropdown } from "primeng/dropdown";
import { TicketCategoryService } from "../service/ticket-category.service";
import { TicketSubCategoryService } from "../service/ticket-sub-category.service";
import { ITicketSubCategory } from "../model/ticketSubCategoryDto";

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
  pickDate;
  pickTime;
  userRole;

  fileName: string = "";
  ticketAttachment?: File[] = [];
  ticketEditParams?;

  //   FORM GROUP TICKET FORM
  ticketForm!: FormGroup;
  ticketType?: string[];
  fileList: File[] = [];
  subCategories

  @ViewChild("fileInput") fileInput: ElementRef;
  @ViewChild("dropdown") dropdown?: Dropdown;
  @ViewChild("dropdown1") dropdown1?: Dropdown;
  @ViewChild("dropdown2") dropdown2?: Dropdown;
  @ViewChild("dropdown3") dropdown3?: Dropdown;
  @ViewChild("dropdown4") dropdown4?: Dropdown;
  @ViewChild("dropdown5") dropdown5?: Dropdown;
  @ViewChild("dropdown6") dropdown6?: Dropdown;
  @ViewChild("dropdown7") dropdown7?: Dropdown;

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
    private roleService: RolesService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private ticketCategoryService: TicketCategoryService,
    private ticketSubCategoryService: TicketSubCategoryService
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

    this.userRole = this.sessionStorageService.getRoleName();

    this.patchUserDetials();
  }

  patchUserDetials() {
    if (this.editMode == false) {
      this.ticketForm.patchValue({
        name: localStorage.getItem("loginUserName"),
        email: localStorage.getItem("loginUserEmail"),
      });
    }
  }

  ngAfterViewChecked(): void {
    if (this.ticketForm.get("ticketType").value == "Pickup Request") {
      const dropdowns = [
        this.dropdown,
        this.dropdown1,
        this.dropdown2,
        this.dropdown3,
      ];

      dropdowns.forEach((dropdown, index) => {
        if (dropdown) {
          (dropdown.filterBy as any) = {
            split: (_: any) => [(item: any) => item],
          };
        }
      });
    } else if (this.ticketForm.get("ticketType")?.value == "Rate Inquiry") {
      const dropdowns = [
        this.dropdown4,
        this.dropdown5,
        this.dropdown6,
        this.dropdown7,
      ];

      dropdowns.forEach((dropdown, index) => {
        if (dropdown) {
          (dropdown.filterBy as any) = {
            split: (_: any) => [(item: any) => item],
          };
        }
      });
    }
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
      subCategory: new FormControl(null, Validators.required),
      ticketStatus: new FormControl(null, Validators.required),
      ticketFlag: new FormControl(null, Validators.required),
      deliveryStreetName: new FormControl(null),
      deliveryDistrict: new FormControl(null),
      pickupStreetName: new FormControl(null),
      pickupDistrict: new FormControl(null),
      ticketType: new FormControl(null, Validators.required),
      weight: new FormControl(null),
      name: new FormControl(null, Validators.required),
      email: new FormControl(null, [Validators.required, Validators.email]),
      phone: new FormControl(null, Validators.required),
      textarea: new FormControl(null),
      airwayNumber: new FormControl(null),
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
      this.ticketForm.get("textarea").disable();
      this.ticketForm.get("name").disable();
      this.ticketForm.get("email").disable();
      this.ticketForm.get("ticketType").disable();

      this._ticketService.getSingleTicket(this.editId).subscribe((res) => {
        this.singleTicket = res;

        const filePaths = this.singleTicket.attachments.map(
          (attachment) => attachment.filePath
        );

        const FileNames = filePaths.join(",");

        this.ticketEditParams = { fileNames: FileNames };

        for (let i = 0; i < this.singleTicket?.attachments?.length; i++) {
          this.attachAgreement(
            this.singleTicket?.attachments[i]?.filePath,
            this.singleTicket?.attachments[i]?.filePath.split("/").pop()
          );
        }

        this.getAllCitiesAndDepartmentCategories(
          this.singleTicket.destinationCountry,
          this.singleTicket.originCountry,
          this.singleTicket.department
        );

        this.getDepartmentCategory(this.singleTicket?.departmentCategory);
        this.getTicketCategory(this.singleTicket?.ticketCategory);

        if (
          this.singleTicket.pickupTime != null &&
          this.singleTicket.pickupDate != null
        ) {
          this.pickDate = new Date(this.singleTicket.pickupDate);
          let pickTimeArray = this.singleTicket.pickupTime;
          this.pickTime = new Date(`2023-11-12 ${pickTimeArray}`);
        } else {
          this.pickDate = null;
          this.pickTime = null;
        }

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
          pickupDate: this.pickDate,
          pickupTime: this.pickTime,
          department: this.singleTicket.department,
          departmentCategory: this.singleTicket.departmentCategory,
          assignedTo: this.singleTicket.assignedTo,
          category: this.singleTicket.ticketCategory,
          subCategory: this.singleTicket.ticketSubCategory,
          ticketStatus: this.singleTicket.ticketStatus,
          ticketFlag: this.singleTicket.ticketFlag,
          deliveryStreetName: this.singleTicket.deliveryStreetName,
          deliveryDistrict: this.singleTicket.deliveryDistrict,
          pickupStreetName: this.singleTicket.pickupStreetName,
          pickupDistrict: this.singleTicket.pickupDistrict,
          ticketType: this.singleTicket.ticketType,
          weight: this.singleTicket.weight,
          name: this.singleTicket.name,
          email: this.singleTicket.email,
          phone: this.singleTicket.phone,
          textarea: this.singleTicket.textarea,
          airwayNumber: this.singleTicket.airwayNumber,
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
        this.productFields.filter((data) => data?.name == "Category")[0]
          .productFieldValuesList
      );

      this.ticketFlag = this.dropdownService.extractNames(
        this.productFields.filter((data) => data?.name == "Ticket Flag")[0]
          .productFieldValuesList
      );

      this.status = this.dropdownService.extractNames(
        this.productFields.filter((data) => data?.name == "Ticket Status")[0]
          .productFieldValuesList
      );

      this.ticketType = this.dropdownService.extractNames(
        this.productFields.filter((data) => data?.name == "Ticket Type")[0]
          .productFieldValuesList
      );
    });

    // Get All Countries

    this.countryService.getAllCountries(this.params).subscribe((res?: any) => {
      this.countries = res;
      this.countries = this.dropdownService.extractNames(this.countries);
    });

    // Get All Departments
    this.departmentService
      .getDepartments(this.params)
      .subscribe((res?: any) => {
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
    this.cityService.getAllCities(this.params).subscribe((res?: any) => {
      this.destinationCities = res;
      this.originCities = res;
      //   Destination Cities
      let filterDestinationCities = this.destinationCities.filter(
        (value) => value?.country?.name == destinationCountry
      );
      this.destinationCities = this.dropdownService.extractNames(
        filterDestinationCities
      );

      //   Origin Cities
      let filterOriginCities = this.originCities.filter(
        (value) => value?.country?.name == originCountry
      );
      this.originCities = this.dropdownService.extractNames(filterOriginCities);
    });

    // FOR DEPARTMENT CATEGORY
    this.departmentCategoryService
      .getDepartmentCategories(this.params)
      .subscribe((res?: any) => {
        this.departmentCategory = res.body;
        let filterDepartments = this.departmentCategory.filter(
          (city) => city?.department?.name == department
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
      .subscribe((res?: any) => {
        this.departmentCategory = res.body;
        let filterDepartments = this.departmentCategory.filter(
          (city) => city?.department?.name == department.value
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

  getDepartmentCategory(departmentCategory) {
    this.ticketCategoryService
      .getTicketCategories(this.params)
      .subscribe((res) => {
        this.categories = res.body;
        let filterCategories = this.categories.filter(
          (ticketCategory) =>
            ticketCategory?.departmentCategory?.name == departmentCategory
        );

        // Disable
        if (filterCategories.length == 0) {
          this.ticketForm.get("category")?.disable();
        } else {
          this.ticketForm.get("category")?.enable();
        }

        this.categories = this.dropdownService.extractNames(filterCategories);
      });
  }

  getTicketCategory(TicketCategory) {
    this.ticketSubCategoryService
      .getTicketSubCategories(this.params)
      .subscribe((res) => {
        this.subCategories = res.body;
        let filterSubCategories = this.subCategories.filter(
          (ticketSubCategory: ITicketSubCategory) =>
            ticketSubCategory?.ticketCategory?.name == TicketCategory
        );

        // Disable
        if (filterSubCategories.length == 0) {
          this.ticketForm.get("subCategory")?.disable();
        } else {
          this.ticketForm.get("subCategory")?.enable();
        }

        this.subCategories =
          this.dropdownService.extractNames(filterSubCategories);
      });
  }

  //   GET DESTINATION COUNTRY FROM DROPDOWN
  getDestinationCountry(country) {
    this.cityService.getAllCities(this.params).subscribe((res?: any) => {
      this.destinationCities = res;
      let filterCities = this.destinationCities.filter(
        (city) => city?.country?.name == country.value
      );
      this.destinationCities = this.dropdownService.extractNames(filterCities);
    });
  }

  //   GET ORGIN COUNTRYFROM DROPDOWN
  getOriginCountry(country) {
    this.cityService.getAllCities(this.params).subscribe((res?: any) => {
      this.originCities = res;
      let filterCities = this.originCities.filter(
        (city) => city?.country?.name == country.value
      );
      this.originCities = this.dropdownService.extractNames(filterCities);
    });
  }

  //  ON TICKET FORM SUBMIT

  onSubmit() {
    if (
      this.ticketForm.get("phone")?.valid &&
      (this.ticketForm.get("ticketType")?.valid ||
        this.ticketForm.get("ticketType")?.disabled) &&
      (this.ticketForm.get("email")?.valid ||
        this.ticketForm.get("email")?.disabled) &&
      (this.ticketForm.get("name")?.valid ||
        this.ticketForm.get("name")?.disabled)
    ) {
      // Date and Time get from form
      let ticketDate: Date = this.ticketForm.value.pickupDate;
      let ticketT: Date = this.ticketForm.value.pickupTime;
      let formattedDate: string = this._ticketService.formatDate(ticketDate);
      const ticketTime: string =
        this._ticketService.formatDateToHHMMSS(ticketT);

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
        ticketCategory: formValue.category,
        ticketSubCategory: formValue.subCategory,
        ticketStatus: formValue.ticketStatus,
        ticketFlag: formValue.ticketFlag,
        department: formValue.department,
        departmentCategory: formValue.departmentCategory,
        deliveryStreetName: formValue.deliveryStreetName,
        deliveryDistrict: formValue.deliveryDistrict,
        pickupStreetName: formValue.pickupStreetName,
        pickupDistrict: formValue.pickupDistrict,
        ticketType: this.ticketForm.get("ticketType")?.value,
        weight: formValue.weight,
        name: this.ticketForm.get("name")?.value,
        email: this.ticketForm.get("email")?.value,
        phone: formValue.phone,
        textarea: this.ticketForm.get("textarea")?.value,
        airwayNumber: formValue.airwayNumber,
        createdBy: localStorage.getItem("loginUserEmail"),
      };

      if (this.ticketForm.get("ticketType")?.value === "Pickup Request") {
        if (
          this.ticketForm.get("pickupDate")?.valid &&
          this.ticketForm.get("pickupTime")?.valid
        ) {
          if (this.editMode) {
            this._ticketService
              .editTicket(
                this.editId,
                ticketData,
                this.ticketAttachment,
                this.ticketEditParams
              )
              .subscribe(() => {
                this.update();
                this.router.navigate(["ticket/list"]);
              });
          } else {
            //   Create Ticket

            this._ticketService
              .createTicket(ticketData, this.ticketAttachment)
              .subscribe((res: any) => {
                this.success();
                this.router.navigate(["ticket/list"]);
                this.ticketForm.reset();
              });
          }
        } else {
          this.alert();
          this.formService.markFormGroupTouched(this.ticketForm);
        }
      } else {
        if (this.editMode) {
          this._ticketService
            .editTicket(
              this.editId,
              ticketData,
              this.ticketAttachment,
              this.ticketEditParams
            )
            .subscribe(() => {
              this.update();
              this.router.navigate(["ticket/list"]);
            });
        } else {
          //   Create Ticket

          this._ticketService
            .createTicket(ticketData, this.ticketAttachment)
            .subscribe((res: any) => {
              this.success();
              this.router.navigate(["ticket/list"]);
              this.ticketForm.reset();
            });
        }
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.ticketForm);
    }
  }

  onFileChange(event: any): void {
    const fileInput = event.target;

    if (fileInput.files && fileInput.files.length > 0) {
      const selectedFiles = fileInput.files;

      // Check file extensions
      const allowedExtensions = /\.(pdf|jpg|jpeg|png|gif)$/i;
      let isValid = true;

      for (let i = 0; i < selectedFiles.length; i++) {
        const file = selectedFiles[i];
        if (!allowedExtensions.test(file.name)) {
          isValid = false;
          break;
        }
      }

      if (isValid) {
        this.fileName = selectedFiles[0].name;
        for (let i = 0; i < selectedFiles.length; i++) {
          this.ticketAttachment.push(selectedFiles[i]);
        }
        this.updateFileInput();
      } else {
        this.updateFileInput();
        this.messageService.add({
          severity: "error",
          summary: "Invalid File",
          detail: "Only PDF, JPG, JPEG, PNG, GIF files are allowed.",
        });
      }
    } else {
      this.updateFileInput();
      this.messageService.add({
        severity: "warn",
        summary: "No File Selected",
      });
    }
  }

  removeFile(index: number): void {
    this.ticketAttachment.splice(index, 1);
    this.updateFileInput();
  }

  updateFileInput(): void {
    const fileInput = document.getElementById("attachment") as HTMLInputElement;
    if (fileInput) {
      const newFileList = new DataTransfer();
      for (const file of this.ticketAttachment) {
        newFileList.items.add(file);
      }
      fileInput.files = newFileList.files;
    }
  }

  clearFileInput(): void {
    // Reset the file input field
    const fileInput = document.getElementById("attachment") as HTMLInputElement;
    if (fileInput) {
      fileInput.value = null;
    }
  }

  attachAgreement(attachmentUrl: string, fileName: string) {
    this.accountService.downloadAgreement(attachmentUrl).subscribe(
      (blob: Blob) => {
        const file = new File([blob], `${fileName}`, {
          type: "application/pdf",
        });
        this.attachFiles(file);
      },
      (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: "Attachment Not Found",
        });
      }
    );
  }

  attachFiles(file: File) {
    // Add the file to the global array
    this.fileList.push(file);

    // Set the files property of the input element
    const dataTransfer = new DataTransfer();
    this.fileList.forEach((f, index) => {
      dataTransfer.items.add(f);
    });
    this.fileInput.nativeElement.files = dataTransfer.files;
    this.ticketAttachment = this.fileList;
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
