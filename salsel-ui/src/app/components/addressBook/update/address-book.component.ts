import { Component, ViewChild } from "@angular/core";
import { IAddressBook, AddressBook } from "../model/addressBookDto";
import { AddressBookService } from "../service/address-book.service";
import { HttpClient } from "@angular/common/http";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { DropdownService } from "src/app/layout/service/dropdown.service";


import { Dropdown } from "primeng/dropdown";
import { CityService } from "../../City/service/city.service";
import { CountryService } from "../../country/service/country.service";

@Component({
  selector: "app-address-book",
  templateUrl: "./address-book.component.html",
  styleUrls: ["./address-book.component.scss"],
  providers: [MessageService],
})
export class AddressBookComponent {
  @ViewChild("dropdown") dropdown?: Dropdown;
  @ViewChild("dropdown1") dropdown1?: Dropdown;

  addressBookForm!: FormGroup;
  addressBook?: IAddressBook;
  addressBookId?: any;
  mode?: string = "Add";
  userTypes?;

  productFields?;
  countries;
  cities;

  constructor(
    private addressBookService: AddressBookService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
    private formService: FormvalidationService,
    private messageService: MessageService,
    private dropDownService: DropdownService,
    private countryService: CountryService,
    private cityService: CityService
  ) {}

  ngOnInit(): void {
    this.addressBookForm = this.fb.group({
      name: [null, Validators.required],
      contactNumber: [null, Validators.required],
      address: [null, Validators.required],
      streetName: [null, Validators.required],
      district: [null, Validators.required],
      refNumber: [null],
      country: [null, Validators.required],
      city: [null, Validators.required],
      userType: [null, Validators.required],
    });

    this.getProductFieldValues();

    this.route.queryParams.subscribe((params) => {
      this.addressBookId = params["id"];
      if (this.addressBookId) {
        this.mode = "Update";
        this.updateForm(this.addressBookId);
      }
    });
  }

  ngAfterViewInit(): void {
    const dropdowns = [this.dropdown, this.dropdown1];

    dropdowns.forEach((dropdown, index) => {
      if (dropdown) {
        (dropdown.filterBy as any) = {
          split: (_: any) => [(item: any) => item],
        };
      }
    });
  }

  getProductFieldValues() {
    this.dropDownService.getAllProductFields().subscribe((res: any) => {
      this.productFields = res;

      this.userTypes = this.dropDownService.extractNames(
        this.productFields.filter((data: any) => data?.name == "User Type")[0]
          ?.productFieldValuesList
      );
    });

    this.countryService
      .getAllCountries({ status: true })
      .subscribe((res: any) => {
        this.countries = res;
        this.countries = this.dropDownService.extractNames(this.countries);
      });
  }

  onSubmit() {
    if (this.addressBookForm && this.addressBookForm.valid) {
      this.addressBook = this.createFromForm();
      if (this.mode == "Update") {
        this.addressBookService
          .update(this.addressBook, this.addressBookId)
          .subscribe((res: any) => {
            this.router.navigate(["address-book/list"]);
          });
      } else {
        this.addressBookService.create(this.addressBook).subscribe((res) => {
          if (res && res.body) {
            this.router.navigate(["address-book/list"]);
          }
        });
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.addressBookForm);
    }
  }

  updateForm(id?: any) {
    this.getAddressBookById(id);
  }

  getAddressBookById(id?: any) {
    this.addressBookService.getAddressBookById(id).subscribe((res) => {
      if (res && res.body) {
        this.addressBook = res.body;
        this.patchCity(this.addressBook?.country);
        this.patchFormWithDto();
      }
    });
  }

  patchFormWithDto() {
    this.addressBookForm.patchValue({
      name: this.addressBook?.name,
      contactNumber: this.addressBook?.contactNumber,
      refNumber: this.addressBook?.refNumber,
      streetName: this.addressBook?.streetName,
      district: this.addressBook?.district,
      address: this.addressBook?.address,
      country: this.addressBook?.country,
      city: this.addressBook?.city,
      userType: this.addressBook?.userType,
    });
  }

  patchCity(country?: any) {
    this.cityService.getAllCitiesByCountryName(country).subscribe((res) => {
      if (res) {
        this.cities = res;
        this.cities = this.dropDownService.extractNames(this.cities);
      }
    });
  }

  createFromForm() {
    const formValue = this.addressBookForm.value;

    const addressBook: IAddressBook = {
      id: this.addressBookId ? this.addressBookId : undefined,
      name: formValue.name,
      contactNumber: formValue.contactNumber,
      refNumber: formValue.refNumber,
      streetName: formValue.streetName,
      district: formValue.district,
      address: formValue.address,
      country: formValue.country,
      city: formValue.city,
      userType: formValue.userType,
    };

    return addressBook;
  }

  onSelectCountry(data: any) {
    const country = data.value;

    this.cityService.getAllCitiesByCountryName(country).subscribe((res) => {
      if (res) {
        this.cities = res;
        this.cities = this.dropDownService.extractNames(this.cities);
      }
    });
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }

  onCancel() {
    this.router.navigate(["address-book/list"]);
  }
}
