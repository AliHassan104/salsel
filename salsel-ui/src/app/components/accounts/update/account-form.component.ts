import { IAccountData } from "../model/accountValuesDto";
import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { MessageService } from "primeng/api";
import { AccountService } from "../service/account.service";
import { ActivatedRoute, Router } from "@angular/router";
import { DropdownService } from "src/app/service/dropdown.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { CityService } from "../../City/service/city.service";
import { CountryService } from "../../country/service/country.service";

@Component({
  selector: "app-account-form",
  templateUrl: "./account-form.component.html",
  styleUrls: ["./account-form.component.scss"],
  providers: [MessageService],
})
export class AccountFormComponent implements OnInit {
  accountForm!: FormGroup;

  productFields?;

  accountTypes?;
  countries?;
  cities?;
  salesRegion?;
  salesAgents?;

  fileName: string = "";

  singleAccount: IAccountData;
  editMode;
  editId: any;
  params: any = { status: true };
  ageement: any;

  constructor(
    private messageService: MessageService,
    private accountService: AccountService,
    private route: ActivatedRoute,
    private dropdownService: DropdownService,
    private formService: FormvalidationService,
    private router: Router,
    private cityService: CityService,
    private countryService: CountryService
  ) {}

  ngOnInit(): void {
    // Form Setup
    this.FormSetup();

    // QUERY PARAMS FOR EDIT PURPOSE
    this.queryParamsId();

    this.getAllProductFields();
  }

  //   Get All Cities
  getAllCities(countryName) {
    this.cityService.getAllCities(this.params).subscribe((res) => {
      this.cities = res;
      let filterCities = this.cities.filter(
        (city) => city.country.name == countryName
      );
      this.cities = this.dropdownService.extractNames(filterCities);
    });
  }

  FormSetup() {
    this.accountForm = new FormGroup({
      accountNumber: new FormControl(null, Validators.required),
      accountType: new FormControl(null, Validators.required),
      customerName: new FormControl(null, Validators.required),
      contactNumber: new FormControl(null, Validators.required),
      address: new FormControl(null, Validators.required),
      businessActivity: new FormControl(null, Validators.required),
      projectName: new FormControl(null, Validators.required),
      tradeLicenseNo: new FormControl(null, Validators.required),
      taxDocumentNo: new FormControl(null, Validators.required),
      county: new FormControl(null, Validators.required),
      city: new FormControl(null, Validators.required),
      custName: new FormControl(null, Validators.required),
      billingPocName: new FormControl(null, Validators.required),
      salesRegion: new FormControl(null, Validators.required),
      salesAgentName: new FormControl(null, Validators.required),
      agreement: new FormControl(null),
    });
  }

  // Forms Values Edit
  editForm() {
    this.accountForm.setValue({
      accountNumber: this.singleAccount.accountNumber,
      accountType: this.singleAccount.accountType,
      customerName: this.singleAccount.customerName,
      contactNumber: this.singleAccount.contactNumber,
      address: this.singleAccount.address,
      businessActivity: this.singleAccount.businessActivity,
      projectName: this.singleAccount.projectName,
      tradeLicenseNo: this.singleAccount.tradeLicenseNo,
      taxDocumentNo: this.singleAccount.taxDocumentNo,
      county: this.singleAccount.county,
      city: this.singleAccount.city,
      custName: this.singleAccount.custName,
      billingPocName: this.singleAccount.billingPocName,
      salesRegion: this.singleAccount.salesRegion,
      salesAgentName: this.singleAccount.salesAgentName,
    });
  }

  //   Get id from query params

  queryParamsId() {
    this.route.queryParams.subscribe((params) => {
      // Retrieve editMode and id from the query parameters
      if (params["id"] != null) {
        this.editMode = params["updateMode"] === "true"; // Convert to boolean
        this.editId = +params["id"]; // Convert to number
      } else {
        this.editMode = false;
      }
    });

    if (this.editId != null) {
      this.accountService.getSingleAccount(this.editId).subscribe((res) => {
        this.singleAccount = res;
        this.getAllCities(this.singleAccount.county);
        this.editForm();
      });
    }
  }

  //   GET ALL PRODUCT FIELDS
  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productFields = res;

      this.accountTypes = this.dropdownService.extractNames(
        this.productFields.filter((data) => data.name == "Account Types")[0]
          .productFieldValuesList
      );

      this.salesRegion = this.dropdownService.extractNames(
        this.productFields.filter((data) => data.name == "Sales Region")[0]
          .productFieldValuesList
      );

      this.salesAgents = this.dropdownService.extractNames(
        this.productFields.filter((data) => data.name == "Sales Agent")[0]
          .productFieldValuesList
      );
    });

    this.countryService.getAllCountries(this.params).subscribe((res) => {
      this.countries = res;
      this.countries = this.dropdownService.extractNames(this.countries);
    });
  }

  //   GET COUNTRY FROM DROPDOWN
  getCountry(country) {
    this.cityService.getAllCities(this.params).subscribe((res) => {
      this.cities = res;
      let filterCities = this.cities.filter(
        (city) => city.country.name == country.value
      );
      this.cities = this.dropdownService.extractNames(filterCities);
    });
  }

  onSubmit(data: IAccountData) {
    if (this.accountForm.valid) {
      if (this.editMode) {
        this.accountService.editAccount(this.editId, data).subscribe((res) => {
          this.accountForm.reset();
          this.router.navigate(["account/list"]);
        });
      } else {
        // this.accountService.addAccount(data).subscribe((res) => {
        //   this.accountForm.reset();
        //   this.router.navigate(["account/list"]);
        // });
        console.log(data);
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.accountForm);
    }
  }

  onFileChange(event: any): void {
    const fileInput = event.target;
    if (fileInput.files && fileInput.files.length > 0) {
      const selectedFile = fileInput.files[0];

      // Check if the selected file has a PDF extension
      if (selectedFile.name.toLowerCase().endsWith(".pdf")) {
        this.fileName = selectedFile.name;
        this.ageement = selectedFile;
        console.log(this.ageement);

        // You can also store the file itself if needed: this.accountForm.patchValue({ agreement: selectedFile });
      } else {
        // Clear the file input and display an error message
        this.accountForm.get("agreement").setValue(null);
        fileInput.value = null;

        this.messageService.add({
          severity: "error",
          summary: "Invalid File",
          detail: "Only PDF files are allowed.",
        });
      }
    }
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }

  //   On cancel edit request
  onCancel() {
    this.router.navigate(["account/list"]);
  }
}
