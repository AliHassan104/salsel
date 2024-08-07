import { IAccountData } from "../model/accountValuesDto";
import {
  AfterViewInit,
  Component,
  ElementRef,
  OnInit,
  ViewChild,
} from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { MessageService } from "primeng/api";
import { AccountService } from "../service/account.service";
import { ActivatedRoute, Router } from "@angular/router";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { CityService } from "../../City/service/city.service";
import { CountryService } from "../../country/service/country.service";
import { filter } from "rxjs";
import { UserService } from "../../auth/usermanagement/service/user.service";
import { Dropdown } from "primeng/dropdown";

@Component({
  selector: "app-account-form",
  templateUrl: "./account-form.component.html",
  styleUrls: ["./account-form.component.scss"],
  providers: [MessageService],
})
export class AccountFormComponent implements OnInit, AfterViewInit {
  accountForm!: FormGroup;

  productFields?;

  accountTypes?;
  countries?;
  cities?;
  salesRegion?;
  salesAgents?;

  fileName: string = "";

  singleAccount?: IAccountData;
  editMode;
  editId: any;
  params: any = { status: true };

  accountAgeement: any;
  agreement;
  allAgreements;

  @ViewChild("fileInput") fileInput: ElementRef;
  @ViewChild("dropdown") dropdown?: Dropdown;
  @ViewChild("dropdown1") dropdown1?: Dropdown;

  constructor(
    private messageService: MessageService,
    private accountService: AccountService,
    private route: ActivatedRoute,
    private dropdownService: DropdownService,
    private formService: FormvalidationService,
    private router: Router,
    private cityService: CityService,
    private countryService: CountryService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    // Form Setup
    this.FormSetup();

    // QUERY PARAMS FOR EDIT PURPOSE
    this.queryParamsId();

    this.getAllProductFields();
  }

  ngAfterViewInit(): void {
    if (this.dropdown) {
      (this.dropdown.filterBy as any) = {
        split: (_: any) => [(item: any) => item],
      };
    }
    if (this.dropdown1) {
      (this.dropdown1.filterBy as any) = {
        split: (_: any) => [(item: any) => item],
      };
    }
  }

  //   Get All Cities
  getAllCities(countryName) {
    this.cityService.getAllCities(this.params).subscribe((res: any) => {
      this.cities = res;
      let filterCities = this.cities.filter(
        (city) => city?.country?.name == countryName
      );
      this.cities = this.dropdownService.extractNames(filterCities);
    });
  }

  FormSetup() {
    this.accountForm = new FormGroup({
      //   accountNumber: new FormControl(null, Validators.required),
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
      email: new FormControl(null, [Validators.required, Validators.email]),
    });
  }

  // Forms Values Edit
  editForm() {
    this.accountForm.setValue({
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
      email: this.singleAccount.email,
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
      this.accountService
        .getSingleAccount(this.editId)
        .subscribe((res: any) => {
          this.singleAccount = res;
          this.attachAgreement(this.singleAccount.accountUrl);

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
        this.productFields.filter((data) => data?.name == "Account Types")[0]
          .productFieldValuesList
      );

      this.salesRegion = this.dropdownService.extractNames(
        this.productFields.filter((data) => data?.name == "Sales Region")[0]
          .productFieldValuesList
      );

      this.userService.getAllUser({ status: true }).subscribe((res: any) => {
        this.salesAgents = res;

        const filteredData = this.salesAgents.filter(
          (item) => item?.roles[0]?.name === "ROLE_SALES_AGENT"
        );
        this.salesAgents = this.dropdownService.extractNames(filteredData);
      });
    });

    this.countryService.getAllCountries(this.params).subscribe((res) => {
      this.countries = res;
      this.countries = this.dropdownService.extractNames(this.countries);
    });
  }

  //   GET COUNTRY FROM DROPDOWN
  getCountry(country: any) {
    this.cityService.getAllCities(this.params).subscribe((res: any) => {
      this.cities = res;
      let filterCities = this.cities.filter(
        (city: any) => city?.country?.name == country.value
      );
      this.cities = this.dropdownService.extractNames(filterCities);
    });
  }

  onSubmit(data: IAccountData) {
    if (
      this.accountForm.valid &&
      this.fileInput.nativeElement.files.length != 0
    ) {
      if (this.editMode) {
        this.accountService
          .editAccount(this.editId, data, this.accountAgeement)
          .subscribe((res: any) => {
            this.accountForm.reset();
            this.router.navigate(["account/list"]);
          });
      } else {
        this.accountService.addAccount(data, this.accountAgeement).subscribe(
          (res: any) => {
            this.accountForm.reset();
            this.router.navigate(["account/list"]);
          },
          (error) => {
            this.messageService.add({
              severity: "error",
              summary: "Warning",
              detail: error?.error?.error,
            });
          }
        );
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.accountForm);
    }
  }

  attachAgreement(agreementUrl: string) {
    this.accountService.downloadAgreement(agreementUrl).subscribe(
      (blob: Blob) => {
        const fileName = agreementUrl.split("/").pop();
        const file = new File([blob], fileName, {
          type: "application/pdf",
        });
        // Create a DataTransfer object
        const dataTransfer = new DataTransfer();

        // Add the file to the DataTransfer object
        dataTransfer.items.add(file);

        // Set the files property of the input element
        this.fileInput.nativeElement.files = dataTransfer.files;
        this.accountAgeement = file;
      },
      (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: "Error Attaching File",
        });
      }
    );
  }

  onFileChange(event: any): void {
    const fileInput = event.target;
    if (fileInput.files && fileInput.files.length > 0) {
      const selectedFile = fileInput.files[0];

      // Check if the selected file has a PDF extension
      if (selectedFile.name.toLowerCase().endsWith(".pdf")) {
        this.fileName = selectedFile.name;
        this.accountAgeement = selectedFile;
      } else {
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
      detail:
        "Please ensure that all required details are filled out and Agreement is attached.",
    });
  }

  //   On cancel edit request
  onCancel() {
    this.router.navigate(["account/list"]);
  }
}
